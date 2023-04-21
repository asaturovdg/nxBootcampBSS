package ru.wostarnn.nxbootcampbss.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

// Класс тарифа для работы с БД. Данное количество полей позволяет довольно гибко настраивать тарифы с пакетом минут
// (прим. задав inboundRateOnExtraMinutes/outboundRateOnExtraMinutes отрицательным значением можно указать,
// что пакет минут не расходуется на исходящие/входящие вызовы)
// При добавлении новых тарифов с нестандартными возможностями можно будет добавить новые поля в классе и БД

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
