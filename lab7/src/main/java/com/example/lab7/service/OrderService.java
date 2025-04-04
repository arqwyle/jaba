package com.example.lab7.service;

import com.example.lab7.model.Address;
import com.example.lab7.model.Order;
import com.example.lab7.model.Payment;
import com.example.lab7.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> findOrdersByCustomerAddress(Address address) {
        return orderRepository.findByCustomerAddress(address);
    }

    public List<Order> findOrdersByTimeInterval(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByDateBetween(startDate, endDate);
    }

    public List<Order> findOrdersByPaymentMethod(Class<? extends Payment> type) {
        return orderRepository.findByPaymentMethod(type);
    }

    public List<Order> findOrdersByCustomerName(String customerName) {
        return orderRepository.findByCustomerName(customerName);
    }

    public List<Order> findOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}
