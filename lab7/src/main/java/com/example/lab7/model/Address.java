package com.example.lab7.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String city;
    private String street;
    private String zipcode;
}
