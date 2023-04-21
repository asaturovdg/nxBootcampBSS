package ru.wostarnn.nxbootcampbss.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillingResponseBodyNumber {
    private String phoneNumber;
    private double ballance;
}
