package com.example.lab7.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class Quantity extends Measurement {

    private BigDecimal value;
}
