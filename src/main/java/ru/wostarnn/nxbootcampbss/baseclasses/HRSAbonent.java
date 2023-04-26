package ru.wostarnn.nxbootcampbss.baseclasses;

import lombok.AllArgsConstructor;
import lombok.Data;

// Класс абонента для HRS, который помогает обсчитать всех абонентов (особенно тех, чьи тарифы включают в себя пакет минут)
@Data
@AllArgsConstructor
public class HRSAbonent {
    private final String phoneNumber;
    private final String tariff;
    private long extraMinutes;
}
