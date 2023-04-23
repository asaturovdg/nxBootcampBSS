package ru.wostarnn.nxbootcampbss.controllers;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.wostarnn.nxbootcampbss.entities.Abonent;
import ru.wostarnn.nxbootcampbss.requests.PaymentRequestBody;
import ru.wostarnn.nxbootcampbss.response.PaymentResponseBody;
import ru.wostarnn.nxbootcampbss.response.ReportResponseBody;
import ru.wostarnn.nxbootcampbss.services.AbonentService;

@Controller
@RequestMapping("abonent")
public class AbonentController {
    private final AbonentService abonentService;

    public AbonentController(AbonentService abonentService) {
        this.abonentService = abonentService;
    }
    @GetMapping("report/{phoneNumber}")
    @ResponseBody
    public ReportResponseBody report(@PathVariable String phoneNumber) {
        if (phoneNumber.length() == 11) {
            return abonentService.generateReport(phoneNumber);
        }
        return null;
    }

    @PatchMapping(value = "/pay", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PaymentResponseBody payPatch(@Valid @RequestBody PaymentRequestBody requestBody) {
        String numberPhone = requestBody.getNumberPhone();
        double money = requestBody.getMoney();
        Abonent abonent = abonentService.pay(numberPhone, money);
        return new PaymentResponseBody(abonent.getId(), numberPhone, abonent.getBalance());
    }
}
