package com.example.lab7.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;

    @Embedded
    private Quantity quantity;
    private String taxStatus;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
