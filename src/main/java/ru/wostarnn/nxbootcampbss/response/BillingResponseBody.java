package ru.wostarnn.nxbootcampbss.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BillingResponseBody {

    private List<BillingResponseBodyNumber> numbers;
}
