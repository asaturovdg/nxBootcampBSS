package ru.wostarnn.nxbootcampbss.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.wostarnn.nxbootcampbss.entities.Abonent;
import ru.wostarnn.nxbootcampbss.repositories.AbonentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AbonentDAO {
    private final AbonentRepository repository;

    private final CallDAO callDAO;

    public AbonentDAO(AbonentRepository repository, CallDAO callDAO) {
        this.repository = repository;
        this.callDAO = callDAO;
    }

    public Map<String, Abonent> findAllByBalanceGreaterThanToMap(double balance) {
        List<Abonent> abonents = repository.findAllByBalanceGreaterThanOrderByPhoneNumber(balance);
        Map<String, Abonent> abonentMap = new HashMap<>();
        for (Abonent a: abonents) {
            abonentMap.put(a.getPhoneNumber(), a);
        }
        return abonentMap;
    }

    public Abonent findAbonent(String phoneNumber) {
        return repository.findAbonentByPhoneNumber(phoneNumber);
    }

    public Long getMaxId() {
        Long id = repository.findFirstByIdNotNullOrderByIdDesc().getId();
        return id == null ? id : 0;
    }
    public void saveAbonent(Abonent abonent) {
        repository.save(abonent);
    }

    public void saveAll(Iterable<Abonent> abonents) {
        repository.saveAll(abonents);
    }

    @Transactional
    public void deleteAll() {
        callDAO.deleteAll();
        repository.deleteAll();

    }

    public Iterable<Abonent> findAll() {
        return repository.findAll();
    }
}
