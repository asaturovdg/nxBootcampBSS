package ru.wostarnn.nxbootcampbss.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.wostarnn.nxbootcampbss.entities.Abonent;

import java.util.List;

@Repository
    public interface AbonentRepository extends CrudRepository<Abonent, Long> {

        List<Abonent> findAllByBalanceGreaterThanOrderByPhoneNumber(double balance);
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        Abonent findAbonentByPhoneNumber(String phoneNumber);

        Abonent findFirstByIdNotNullOrderByIdDesc();
}
