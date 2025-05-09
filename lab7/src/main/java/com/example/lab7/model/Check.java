package com.example.lab7.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "check_")
@Data
@EqualsAndHashCode(callSuper = false)
public class Check extends Payment {

    private String name;
    private String bankId;
}
