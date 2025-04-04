package com.example.lab7.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@DiscriminatorValue("CASH")
public class Cash extends Payment {

    private BigDecimal cashTendered;
}
