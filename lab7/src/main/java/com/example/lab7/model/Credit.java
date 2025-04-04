package com.example.lab7.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@DiscriminatorValue("CREDIT")
public class Credit extends Payment {

    private String number;
    private String type;
    private LocalDateTime expDate;
}
