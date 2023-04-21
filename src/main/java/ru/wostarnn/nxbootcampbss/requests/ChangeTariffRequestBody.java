package ru.wostarnn.nxbootcampbss.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangeTariffRequestBody {
    @Size(min = 11, max = 11, message = "{validation.name.size.phoneNumber}")
    private String numberPhone;
    @NotBlank
    private String tariff_id;
}
