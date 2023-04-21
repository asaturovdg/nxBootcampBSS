package ru.wostarnn.nxbootcampbss.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportResponseBodyCall {
    private String callType;
    private String startTime;
    private String endTime;
    private String duration;
    private double cost;
}
