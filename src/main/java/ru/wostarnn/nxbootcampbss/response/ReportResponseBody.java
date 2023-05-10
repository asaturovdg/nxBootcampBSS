package ru.wostarnn.nxbootcampbss.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReportResponseBody {
    private Long id;
    private String numberPhone;
    private String tariffIndex;
    private List<ReportResponseBodyCall> payload;
    private double totalCost;
    private String monetaryUnit;

    public ReportResponseBody(String error) {
        this.numberPhone = error;
    }
}
