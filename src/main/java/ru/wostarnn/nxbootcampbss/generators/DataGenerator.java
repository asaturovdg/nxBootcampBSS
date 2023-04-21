package ru.wostarnn.nxbootcampbss.generators;

import com.github.curiousoddman.rgxgen.RgxGen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wostarnn.nxbootcampbss.dao.AbonentDAO;
import ru.wostarnn.nxbootcampbss.entities.Abonent;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Генератор данных для тарификации.
// На данный момент есть возможности:
// ---- Сгенерировать файл CDR ----
//  Формат файла: <тип звонка>, <номер телефона вызывающего>, <номер телефона получателя звонка>, <дата начала>, <дата окончания>
//  - Количество строк в CDR задается переменной cdr.generation.rows в файле application.properties
//  - Количество "уникальных" вызывающих номеров задается переменной cdr.generation.callers в файле application.properties
//  (на самом деле повторения номеров возможны, но вероятность этого достаточно мала + CDR от этого не сломается)
//  - Вызывающие номера остаются в памяти генератора для того, чтобы при следующей генерации (manager/billing) номера остались прежними
//  (это позволяет не добавлять новых абонентов в БД для проверки работоспособности)
//  - Максимальная длительность звонка задается переменной MAX_CALL_DURATION в секундах
//  Для генерации случайных данных используется Random и библиотека RgxGen
//  Путь к файлу относительно .jar задается переменной cdr.path в файле application.properties
//  - Все регулярные выражения для генерации можно посмотреть у переменных класса RgxGen
// ---- Сгенерировать абонентов для БД ----
//  Было решено, что все абоненты, находящиеся в БД, являются абонентами оператора "Ромашка"
//  - Количество абонентов будет равно половине количества "уникальных" вызывающих номеров
//  (для того, чтобы из файла CDR можно было отсеять номера, не находящиеся в БД)
//  - Минимальный и максимальный баланс абонента задается переменными MIN_ABONENT_BALANCE и MAX_ABONENT_BALANCE соответственно
//  - Список возможных тарифов у абонента задается переменной TARIFF_LIST
//  (по-хорошему следовало бы брать список тарифов из БД, но решил пока оставить так)

@Service
public class DataGenerator {
    private final long MAX_CALL_DURATION = 60 * 120; // 120 минут

    private final int MIN_ABONENT_BALANCE = 750;
    private final int MAX_ABONENT_BALANCE = 4000;
    private final List<String> TARIFF_LIST = new ArrayList<>(List.of("03", "06", "11", "0X"));
    @Value("${cdr.path}")
    private String cdrPath;
    @Value("${cdr.generation.rows}")
    private int cdrRowsCount;
    @Value("${cdr.generation.callers}")
    private int cdrCallerPhoneNumbers;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final RgxGen callTypeGen = new RgxGen("0[12]");
    private final RgxGen phoneNumberGen = new RgxGen("79[0-9]{9}");
    private final RgxGen dateGen = new RgxGen("2023(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[0-1])([01][0-9]|2[0-3])([0-5][0-9])([0-5][0-9])");
    private List<String> callers;
    private final AbonentDAO abonentDAO;

    public DataGenerator(AbonentDAO abonentDAO) {
        this.abonentDAO = abonentDAO;
    }

    @Transactional
    void generateCallers() {
        abonentDAO.deleteAll();

        Random random = new Random();
        callers = new ArrayList<>();
        Long id = 0L;

        for (int i = 0; i < cdrCallerPhoneNumbers; i++)
            callers.add(phoneNumberGen.generate());

        List<Abonent> abonents = new ArrayList<>();

        for (int i = 0; i < cdrCallerPhoneNumbers/2; i++) {
            Abonent abonent = new Abonent();
            abonent.setId(++id);
            abonent.setBalance(random.nextInt(MIN_ABONENT_BALANCE,MAX_ABONENT_BALANCE));
            abonent.setPhoneNumber(callers.get(i));
            abonent.setTariff_id(TARIFF_LIST.get(random.nextInt(TARIFF_LIST.size())));
            abonents.add(abonent);
        }
        abonentDAO.saveAll(abonents);
    }
    public void generateCdr() {
        Random random = new Random();

        if (callers == null) {
            generateCallers();
        }

        try {
            File file = new File(cdrPath);
            FileOutputStream fos = new FileOutputStream(file);

            for (int i = 0; i < cdrRowsCount; i++) {

                String firstPart = callTypeGen.generate() + ", "
                        + callers.get(random.nextInt(cdrCallerPhoneNumbers)) + ", "
                        + phoneNumberGen.generate() + ", ";

                String strStart = dateGen.generate();

                LocalDateTime dateStart = LocalDateTime.parse(strStart, formatter);
                LocalDateTime dateEnd = dateStart.plusSeconds(random.nextLong(1, MAX_CALL_DURATION));

                String strEnd = dateEnd.format(formatter);

                String secondPart = dateStart.isBefore(dateEnd)? strStart + ", " + strEnd : strEnd + ", " + strEnd;

                String result = firstPart + secondPart;

                fos.write(result.getBytes());
                fos.write("\n".getBytes());
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
