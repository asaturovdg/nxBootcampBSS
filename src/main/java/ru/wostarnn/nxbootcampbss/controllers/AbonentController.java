package ru.wostarnn.nxbootcampbss.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.wostarnn.nxbootcampbss.entities.Abonent;
import ru.wostarnn.nxbootcampbss.requests.PaymentRequestBody;
import ru.wostarnn.nxbootcampbss.response.PaymentResponseBody;
import ru.wostarnn.nxbootcampbss.response.ReportResponseBody;
import ru.wostarnn.nxbootcampbss.services.AbonentService;

@Controller
@Validated
@RequestMapping("abonent")
public class AbonentController {
    private final AbonentService abonentService;
    @Value("${phone.number.pattern}")
    private String PHONE_NUMBER_PATTERN_INVALID;

    public AbonentController(AbonentService abonentService) {
        this.abonentService = abonentService;
    }
    @GetMapping("report/{phoneNumber}")
    @ResponseBody
    public ReportResponseBody report(@PathVariable String phoneNumber) {
        if (phoneNumber.matches("79[0-9]{9}")) {
            return abonentService.generateReport(phoneNumber);
        }
        return new ReportResponseBody(PHONE_NUMBER_PATTERN_INVALID);
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
