package ru.wostarnn.nxbootcampbss.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChangeTariffResponseBody {
    private Long id;
    private String numberPhone;
    private String tariff_id;
}
