package com.example.lab7.repository;

import com.example.lab7.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByAddress_City(String city);

    List<Order> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByPayment_PaymentType(String paymentType);

    List<Order> findByCustomer_Name(String customerName);

    List<Order> findByStatus(String status);
}
