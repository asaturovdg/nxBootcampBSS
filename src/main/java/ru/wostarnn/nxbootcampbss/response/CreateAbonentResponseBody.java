package ru.wostarnn.nxbootcampbss.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAbonentResponseBody {
    private String numberPhone;
    private String tariff_id;
    private double ballance;
}
