package com.example.lab7.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class Quantity extends Measurement {

    private BigDecimal value;
}
