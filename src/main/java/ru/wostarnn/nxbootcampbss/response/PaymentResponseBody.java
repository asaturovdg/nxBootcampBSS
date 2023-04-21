package ru.wostarnn.nxbootcampbss.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentResponseBody {
    private Long id;
    private String numberPhone;
    private double money;
}
