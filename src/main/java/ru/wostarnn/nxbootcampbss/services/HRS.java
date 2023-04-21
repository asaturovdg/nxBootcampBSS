package ru.wostarnn.nxbootcampbss.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.wostarnn.nxbootcampbss.dao.AbonentDAO;
import ru.wostarnn.nxbootcampbss.dao.CdrPlusDAO;
import ru.wostarnn.nxbootcampbss.dao.TariffDAO;
import ru.wostarnn.nxbootcampbss.entities.Abonent;
import ru.wostarnn.nxbootcampbss.entities.Call;
import ru.wostarnn.nxbootcampbss.entities.HRSAbonent;
import ru.wostarnn.nxbootcampbss.entities.Tariff;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Сервис для обсчета звонков согласно тарифам
// Логика работы не сильно отличается от таковой в отборочном задании
// При добавлении новых тарифов с нестандартными возможностями потребуется
// тщательно продумать новую логику работы методов processCall и valueCall

@Service
public class HRS {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    @Value("${hrs.date.output.format}")
    private String dateOutputFormat;
    @Value("${hrs.duration.output.format}")
    private String durationOutputFormat;
    private final CdrPlusDAO cdrPlusDAO;
    private final AbonentDAO abonentDAO;
    private final TariffDAO tariffDAO;

    private Map<String, Tariff> tariffCache = new HashMap<>();
    private Map<String, Abonent> abonentCache = new HashMap<>();

    public HRS(CdrPlusDAO cdrPlusDAO, AbonentDAO abonentDAO, TariffDAO tariffDAO) {
        this.cdrPlusDAO = cdrPlusDAO;
        this.abonentDAO = abonentDAO;
        this.tariffDAO = tariffDAO;
        updateTariffCache();
        updateAbonentCache();
    }

    private void updateTariffCache() {
        Iterable<Tariff> list = tariffDAO.findAll();
        for (Tariff tariff : list) {
            tariffCache.put(tariff.getId(), tariff);
        }
    }

    private void updateAbonentCache() {
        Iterable<Abonent> list = abonentDAO.findAll();
        for (Abonent abonent : list) {
            abonentCache.put(abonent.getPhoneNumber(), abonent);
        }
    }


    public List<Call> charge() {
        Map<String, HRSAbonent> hrsAbonentMap = new HashMap<>();
        List<String> callsList = cdrPlusDAO.read();
        List<Call> callList = new ArrayList<>();
        for (String line : callsList) {
            String[] splitted = line.split(", ");
            String phoneNumber = splitted[1];
            String tariff = splitted[5];
            HRSAbonent hrsAbonent = hrsAbonentMap.getOrDefault(phoneNumber, new HRSAbonent(phoneNumber, tariff, tariffCache.get(tariff).getExtraMinutes()));
            callList.add(processCall(splitted, hrsAbonent));
            hrsAbonentMap.put(phoneNumber, hrsAbonent);
        }
        return callList;
    }

    private Call processCall(String[] splitted, HRSAbonent hrsAbonent) {
        String type = splitted[0];
        String receiverNumber = splitted[2];
        LocalDateTime callStart = LocalDateTime.parse(splitted[3], formatter);
        LocalDateTime callEnd = LocalDateTime.parse(splitted[4], formatter);

        Duration callDuration = Duration.between(callStart, callEnd);
        long callDurInMinutesCeil = callDuration.toMinutes() + (callDuration.toSecondsPart() == 0 ? 0 : 1);

        double callCost = 0;

        if (type.equals("01")) {
            callCost = valueCall(callDurInMinutesCeil, hrsAbonent, receiverNumber, false);
        } else if (type. equals("02")) {
            callCost = valueCall(callDurInMinutesCeil, hrsAbonent, receiverNumber, true);
        }

        String callStartFormat = callStart.format(DateTimeFormatter.ofPattern(dateOutputFormat));
        String callEndFormat = callEnd.format(DateTimeFormatter.ofPattern(dateOutputFormat));
        String durationFormat = LocalTime.of(callDuration.toHoursPart(), callDuration.toMinutesPart(), callDuration.toSecondsPart())
                .format(DateTimeFormatter.ofPattern(durationOutputFormat));

        return new Call(hrsAbonent.getPhoneNumber(), type, callStartFormat, callEndFormat, durationFormat, callCost);
    }

    private double valueCall(long duration, HRSAbonent hrsAbonent, String receiverNumber, boolean isInbound) {
        Tariff tariff = tariffCache.get(hrsAbonent.getTariff());

        double receiverMattersRate;
        double extraRate;
        double rate;

        if (isInbound) {
            receiverMattersRate = tariff.getInboundRateOnReceiverMatters();
            extraRate = tariff.getInboundRateOnExtraMinutes();
            rate = tariff.getInboundRate();
        } else {
            receiverMattersRate = tariff.getOutboundRateOnReceiverMatters();
            extraRate = tariff.getOutboundRateOnExtraMinutes();
            rate = tariff.getOutboundRate();
        }
        double callCost = 0;

        if (tariff.isReceiverMatters() && abonentCache.containsKey(receiverNumber)) {
            callCost = receiverMattersRate * duration;
            duration = 0;
        }

        long extraMinutes = hrsAbonent.getExtraMinutes();

        if (extraRate >= 0 && duration > 0) {
            long extraDuration = Math.min(extraMinutes, duration);
            callCost += extraRate * extraDuration;
            hrsAbonent.setExtraMinutes(hrsAbonent.getExtraMinutes() - extraDuration);
            duration -= extraDuration;
        }
        callCost += rate * duration;
        return callCost;
    }
}
