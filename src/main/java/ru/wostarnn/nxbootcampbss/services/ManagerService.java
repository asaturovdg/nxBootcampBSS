package ru.wostarnn.nxbootcampbss.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.wostarnn.nxbootcampbss.dao.AbonentDAO;
import ru.wostarnn.nxbootcampbss.entities.Abonent;

// Сервис, которому делегирует задачи контроллер менеджеров (кроме тарификации, ей занимается BRT)

@Service
public class ManagerService {

    private final AbonentDAO abonentDAO;

    public ManagerService(AbonentDAO abonentDAO) {
        this.abonentDAO = abonentDAO;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long changeTariff(String phoneNumber, String tariff) {
        Abonent abonent = abonentDAO.findAbonent(phoneNumber);
        if (abonent == null) {
            return null;
        }
        abonent.setTariff_id(tariff);
        Long id = abonent.getId();
        abonentDAO.saveAbonent(abonent);
        return id;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createAbonent(String phoneNumber, String tariff, double balance) {
        Long id = abonentDAO.getMaxId();

        Abonent abonent = new Abonent();
        abonent.setPhoneNumber(phoneNumber);
        abonent.setTariff_id(tariff);
        abonent.setBalance(balance);
        abonent.setId(++id);
        abonentDAO.saveAbonent(abonent);
    }
}
