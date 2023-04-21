package ru.wostarnn.nxbootcampbss.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wostarnn.nxbootcampbss.dao.AbonentDAO;
import ru.wostarnn.nxbootcampbss.dao.CallDAO;
import ru.wostarnn.nxbootcampbss.dao.CdrDAO;
import ru.wostarnn.nxbootcampbss.dao.CdrPlusDAO;
import ru.wostarnn.nxbootcampbss.generators.DataGenerator;
import ru.wostarnn.nxbootcampbss.entities.Abonent;
import ru.wostarnn.nxbootcampbss.entities.Call;

import java.util.*;

@Service
public class BRT {

    private final AbonentDAO abonentDAO;
    private final CallDAO callDAO;
    private final CdrDAO cdrDAO;
    private final CdrPlusDAO cdrPlusDAO;

    private final DataGenerator dataGenerator;
    private final HRS hrs;
    private Map<String, Abonent> abonentCache;

    public BRT(AbonentDAO abonentDAO, CallDAO callDAO, CdrDAO cdrDAO, CdrPlusDAO cdrPlusDAO, DataGenerator dataGenerator, HRS hrs) {
        this.abonentDAO = abonentDAO;
        this.callDAO = callDAO;
        this.cdrDAO = cdrDAO;
        this.cdrPlusDAO = cdrPlusDAO;
        this.dataGenerator = dataGenerator;
        this.hrs = hrs;
        updateAbonentCache();
    }

    private void updateAbonentCache() {
        abonentCache = abonentDAO.findAllByBalanceGreaterThanToMap(0.00);
    }

    @Transactional
    public Collection<Abonent> performBilling() {

        dataGenerator.generateCdr();

        updateAbonentCache(); //пока руками

        Map<String, Abonent> abonentMap = abonentCache;
        List<String> cdrList = cdrDAO.read();
        List<String> cdrPlusList = new ArrayList<>();

        for (String s : cdrList) {
            String[] line = s.split(", ");
            String phoneNumber = line[1];
            if (abonentMap.containsKey(phoneNumber)) {
                String tariff = abonentMap.get(phoneNumber).getTariff_id();
                cdrPlusList.add(s.concat(", " + tariff));
            }
        }

        createCdrPlus(cdrPlusList);
        List<Call> callList = hrs.charge();

        for (Call call : callList) {
            Abonent abonent = abonentMap.get(call.getPhoneNumber());
            abonent.setBalance(abonent.getBalance() - call.getCost());
            abonentMap.put(call.getPhoneNumber(), abonent);
        }

        callDAO.saveAll(callList);
        abonentDAO.saveAll(abonentMap.values());

        //cache update send

        return abonentMap.values().stream().toList();
    }

    public void createCdrPlus(List<String> strings) {
        cdrPlusDAO.write(strings);
    }
}
