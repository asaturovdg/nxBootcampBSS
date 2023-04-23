package ru.wostarnn.nxbootcampbss.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.wostarnn.nxbootcampbss.dao.AbonentDAO;
import ru.wostarnn.nxbootcampbss.dao.CallDAO;
import ru.wostarnn.nxbootcampbss.dao.TariffDAO;
import ru.wostarnn.nxbootcampbss.entities.Abonent;
import ru.wostarnn.nxbootcampbss.entities.Call;
import ru.wostarnn.nxbootcampbss.response.ReportResponseBody;
import ru.wostarnn.nxbootcampbss.response.ReportResponseBodyCall;

import java.util.ArrayList;
import java.util.List;

// Сервис, которому делегирует задачи контроллер абонентов

@Service
public class AbonentService {

    private final AbonentDAO abonentDAO;
    private final CallDAO callDAO;
    private final TariffDAO tariffDAO;

    public AbonentService(AbonentDAO abonentDAO, CallDAO callDAO, TariffDAO tariffDAO) {
        this.abonentDAO = abonentDAO;
        this.callDAO = callDAO;
        this.tariffDAO = tariffDAO;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Abonent pay(String phoneNumber, double value) {
        Abonent abonent = abonentDAO.findAbonent(phoneNumber);
        if (abonent == null) {
            return null;
        }
        abonent.setBalance(abonent.getBalance() + value);
        abonentDAO.saveAbonent(abonent);
        return abonent;
    }

    @Transactional
    public ReportResponseBody generateReport(String phoneNumber) {
        Abonent abonent = abonentDAO.findAbonent(phoneNumber);
        List<Call> calls = callDAO.findByPhoneNumber(phoneNumber);

        Long id = abonent.getId();
        String tariff = abonent.getTariff_id();
        double totalCost = tariffDAO.findById(tariff).getMonthlyFee();
        String monetaryUnit = "rubles";

        List<ReportResponseBodyCall> responseBodyCalls = new ArrayList<>();
        for (Call call : calls) {
            ReportResponseBodyCall responseBodyCall = new ReportResponseBodyCall(
                    call.getType(),
                    call.getStartTime(),
                    call.getEndTime(),
                    call.getDuration(),
                    call.getCost()
            );
            totalCost += call.getCost();
            responseBodyCalls.add(responseBodyCall);

        }

        return new ReportResponseBody(id, phoneNumber, tariff, responseBodyCalls, totalCost, monetaryUnit);
    }
}
