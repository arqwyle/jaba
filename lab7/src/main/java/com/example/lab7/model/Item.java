package com.example.lab7.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String description;
    @Embedded
    private Weight shippingWeight;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
