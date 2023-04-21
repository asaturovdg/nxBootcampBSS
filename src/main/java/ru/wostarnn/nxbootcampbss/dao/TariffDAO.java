package ru.wostarnn.nxbootcampbss.dao;

import org.springframework.stereotype.Component;
import ru.wostarnn.nxbootcampbss.entities.Tariff;
import ru.wostarnn.nxbootcampbss.repositories.TariffRepository;

@Component
public class TariffDAO {

    private final TariffRepository repository;

    public TariffDAO(TariffRepository repository) {
        this.repository = repository;
    }

    public Iterable<Tariff> findAll() {
        return repository.findAll();
    }

    public Tariff findById(String id) {
        return repository.findTariffById(id);
    }
}
