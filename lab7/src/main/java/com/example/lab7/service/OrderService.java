package com.example.lab7.service;

import com.example.lab7.model.Order;
import com.example.lab7.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> findOrdersByAddressCity(String city) {
        return orderRepository.findByAddress_City(city);
    }

    public List<Order> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByDateBetween(startDate, endDate);
    }

    public List<Order> findOrdersByPaymentType(String paymentType) {
        return orderRepository.findByPayment_PaymentType(paymentType);
    }

    public List<Order> findOrdersByCustomerName(String customerName) {
        return orderRepository.findByCustomer_Name(customerName);
    }

    public List<Order> findOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}
