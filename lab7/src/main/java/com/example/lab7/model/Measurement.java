package com.example.lab7.model;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
public abstract class Measurement {

    private String name;
    private String symbol;
}