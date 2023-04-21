package ru.wostarnn.nxbootcampbss.entities;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class HRSAbonent {
    private final String phoneNumber;
    private final String tariff;
    private long extraMinutes;
}
