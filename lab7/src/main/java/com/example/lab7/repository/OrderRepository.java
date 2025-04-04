package com.example.lab7.repository;

import com.example.lab7.model.Address;
import com.example.lab7.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerAddress(Address address);

    List<Order> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByPayment_dtype(String paymentType);

    List<Order> findByCustomer_Name(String customerName);

    List<Order> findByStatus(String status);
}
