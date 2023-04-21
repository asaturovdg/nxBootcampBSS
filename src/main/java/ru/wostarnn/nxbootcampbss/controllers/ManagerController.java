package ru.wostarnn.nxbootcampbss.controllers;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.wostarnn.nxbootcampbss.entities.Abonent;
import ru.wostarnn.nxbootcampbss.requests.BillingRequestBody;
import ru.wostarnn.nxbootcampbss.requests.ChangeTariffRequestBody;
import ru.wostarnn.nxbootcampbss.requests.CreateAbonentRequestBody;
import ru.wostarnn.nxbootcampbss.response.BillingResponseBody;
import ru.wostarnn.nxbootcampbss.response.BillingResponseBodyNumber;
import ru.wostarnn.nxbootcampbss.response.ChangeTariffResponseBody;
import ru.wostarnn.nxbootcampbss.response.CreateAbonentResponseBody;
import ru.wostarnn.nxbootcampbss.services.BRT;
import ru.wostarnn.nxbootcampbss.services.ManagerService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("manager")
public class ManagerController {

    private final BRT brt;
    private final ManagerService managerService;

    public ManagerController(BRT BRT, ManagerService managerService) {
        this.brt = BRT;
        this.managerService = managerService;
    }

    @PatchMapping(value = "changeTariff", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ChangeTariffResponseBody changeTariff(@Valid @RequestBody ChangeTariffRequestBody requestBody) {
        String phoneNumber = requestBody.getNumberPhone();
        String tariff = requestBody.getTariff_id();
        Long id = managerService.changeTariff(phoneNumber, tariff);
        return new ChangeTariffResponseBody(id, phoneNumber, tariff);
    }

    @PostMapping(value = "abonent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CreateAbonentResponseBody createAbonent(@Valid @RequestBody CreateAbonentRequestBody requestBody) {
        String phoneNumber = requestBody.getNumberPhone();
        String tariff = requestBody.getTariff_id();
        double balance = requestBody.getBallance();
        managerService.createAbonent(phoneNumber, tariff, balance);
        return new CreateAbonentResponseBody(phoneNumber, tariff, balance);
    }

    @PatchMapping(value = "billing", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BillingResponseBody billing(@Valid @RequestBody BillingRequestBody requestBody) {
        if (requestBody.getAction().equals("run")) {
            Collection<Abonent> abonents =  brt.performBilling();
            List<BillingResponseBodyNumber> responseBodyNumbers = new ArrayList<>();
            for (Abonent abonent : abonents) {
                BillingResponseBodyNumber responseBodyNumber = new BillingResponseBodyNumber(abonent.getPhoneNumber(), abonent.getBalance());
                responseBodyNumbers.add(responseBodyNumber);
            }
            return new BillingResponseBody(responseBodyNumbers);
        }
        return null;
    }
}
