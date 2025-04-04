package com.example.lab7.repository;

import com.example.lab7.model.Address;
import com.example.lab7.model.Measurement;
import com.example.lab7.model.Order;
import com.example.lab7.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerAddress(Address address);

    List<Order> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE TYPE(o.payment) = :type")
    List<Order> findByPaymentMethod(@Param("type") Class<? extends Payment> type);

    List<Order> findByCustomerName(String customerName);

    List<Order> findByStatus(String status);
}
