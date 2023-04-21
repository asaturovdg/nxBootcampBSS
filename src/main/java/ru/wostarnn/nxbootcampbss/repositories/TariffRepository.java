package ru.wostarnn.nxbootcampbss.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.wostarnn.nxbootcampbss.entities.Tariff;

public interface TariffRepository extends CrudRepository<Tariff, String> {

    Tariff findTariffById(String id);
}
