package ru.wostarnn.nxbootcampbss.requests;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PaymentRequestBody {
    @Pattern(regexp = "79[0-9]{9}", message = "{numberPhone.Pattern}")
    private String numberPhone;
    @NotNull(message = "{money.NotNull}")
    @Digits(integer = 5, fraction = 2, message = "{money.Digits}")
    @Positive(message = "{money.Positive}")
    private double money;

}
