package ru.wostarnn.nxbootcampbss.entities;

import jakarta.persistence.*;
import lombok.Data;

// Класс абонента для работы с БД
@Entity
@Data
public class Abonent {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Id
    private String phoneNumber;
    private double balance;
    private String tariff_id;
}
