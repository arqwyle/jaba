package com.example.lab7.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Cash extends Payment {

    private BigDecimal cashTendered;
}
