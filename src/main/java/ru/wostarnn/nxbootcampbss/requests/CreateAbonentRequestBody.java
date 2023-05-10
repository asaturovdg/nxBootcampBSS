package ru.wostarnn.nxbootcampbss.requests;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateAbonentRequestBody {
    @Pattern(regexp = "79[0-9]{9}", message = "{numberPhone.Pattern}")
    private String numberPhone;
    @NotBlank(message = "{tariff_id.NotBlank}")
    private String tariff_id;
    @PositiveOrZero(message = "{ballance.PositiveOrZero}")
    private double ballance;
}
