package ru.wostarnn.nxbootcampbss.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BillingRequestBody {
    @NotBlank
    private String action;
}
