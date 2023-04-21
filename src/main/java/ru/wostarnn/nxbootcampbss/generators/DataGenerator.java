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

@Service
public class DataGenerator {
    private final long MAX_CALL_DURATION = 60 * 120; // 120 минут
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
            abonent.setBalance(random.nextInt(750,4000));
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
