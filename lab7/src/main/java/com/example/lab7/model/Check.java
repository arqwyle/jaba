package com.example.lab7.model;

import jakarta.persistence.*;

@DiscriminatorValue("CHECK")
public class Check extends Payment {

    private String name;
    private String bankID;
}
