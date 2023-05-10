package ru.wostarnn.nxbootcampbss.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangeTariffRequestBody {
    @Pattern(regexp = "79[0-9]{9}", message = "{numberPhone.Pattern}")
    private String numberPhone;
    @NotBlank(message = "{tariff_id.NotBlank}")
    private String tariff_id;
}
