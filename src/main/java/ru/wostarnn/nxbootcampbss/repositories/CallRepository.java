package ru.wostarnn.nxbootcampbss.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.wostarnn.nxbootcampbss.entities.Call;

import java.util.List;

public interface CallRepository extends CrudRepository<Call, Long> {

    List<Call> findAllByPhoneNumberOrderByStartTime(String phoneNumber);
}
