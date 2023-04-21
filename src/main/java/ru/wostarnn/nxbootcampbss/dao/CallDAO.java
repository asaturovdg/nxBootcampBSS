package ru.wostarnn.nxbootcampbss.dao;

import org.springframework.stereotype.Component;
import ru.wostarnn.nxbootcampbss.entities.Call;
import ru.wostarnn.nxbootcampbss.repositories.CallRepository;
import java.util.List;

@Component
public class CallDAO {
    private final CallRepository repository;

    public CallDAO(CallRepository repository) {
        this.repository = repository;
    }

    public List<Call> findByPhoneNumber(String phoneNumber) {
        return repository.findAllByPhoneNumberOrderByStartTime(phoneNumber);
    }

    public void saveAll(Iterable<Call> calls) {
        repository.saveAll(calls);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
