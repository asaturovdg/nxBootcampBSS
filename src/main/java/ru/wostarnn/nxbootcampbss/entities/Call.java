package ru.wostarnn.nxbootcampbss.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

// Класс звонка для работы с БД
@Entity
@Data
@NoArgsConstructor
public class Call{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phoneNumber;
    private String type;
    private String startTime;
    private String endTime;
    private String duration;
    private double cost;

    public Call(String phoneNumber, String type, String startTime, String endTime, String duration, double cost) {
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.cost = cost;
    }
}