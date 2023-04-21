package ru.wostarnn.nxbootcampbss.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
public class Tariff {
    @Id
    private String id;
    private double inboundRate;
    private double outboundRate;
    private long extraMinutes;
    private double inboundRateOnExtraMinutes;
    private double outboundRateOnExtraMinutes;
    private boolean receiverMatters;
    private double inboundRateOnReceiverMatters;
    private double outboundRateOnReceiverMatters;
    private double monthlyFee;
}
