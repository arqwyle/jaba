package com.example.lab7.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Credit extends Payment {

    private String number;
    private String type;
    private LocalDateTime expDate;
}
