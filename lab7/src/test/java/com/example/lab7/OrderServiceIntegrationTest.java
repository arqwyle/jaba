package com.example.lab7;

import com.example.lab7.model.*;
import com.example.lab7.repository.CustomerRepository;
import com.example.lab7.repository.OrderRepository;
import com.example.lab7.repository.PaymentRepository;
import com.example.lab7.service.OrderService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
public class OrderServiceIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void findByCustomerAddress() {
        Cash cash = new Cash();
        cash.setAmount(BigDecimal.valueOf(100));
        cash.setCashTendered(BigDecimal.valueOf(100));
        paymentRepository.save(cash);

        Address address = new Address();
        address.setStreet("aboba");
        address.setCity("aboba");
        address.setZipcode("aboba");

        Customer customer = new Customer();
        customer.setName("aboba");
        customer.setAddress(address);
        customerRepository.save(customer);

        Order order = new Order();
        order.setDate(LocalDateTime.now());
        order.setStatus("aboba");
        order.setPayment(cash);
        order.setCustomer(customer);
        orderRepository.save(order);

        List<Order> orders = orderService.findOrdersByCustomerAddress(address);

        assertEquals(1, orders.size());
        assertEquals(orders.get(0).getCustomer().getAddress(), address);
    }

    @Test
    void findOrdersByTimeInterval() {
        Cash cash = new Cash();
        cash.setAmount(BigDecimal.valueOf(100));
        cash.setCashTendered(BigDecimal.valueOf(100));
        paymentRepository.save(cash);

        Address address = new Address();
        address.setStreet("aboba");
        address.setCity("aboba");
        address.setZipcode("aboba");

        Customer customer = new Customer();
        customer.setName("aboba");
        customer.setAddress(address);
        customerRepository.save(customer);

        LocalDateTime now = LocalDateTime.now();
        Order order = new Order();
        order.setDate(now);
        order.setStatus("PAID");
        order.setCustomer(customer);
        order.setPayment(cash);
        orderRepository.save(order);

        LocalDateTime startDate = now.minusHours(1);
        LocalDateTime endDate = now.plusHours(1);
        List<Order> orders = orderService.findOrdersByTimeInterval(startDate, endDate);

        assertEquals(1, orders.size());
        assertEquals(now, orders.get(0).getDate());
    }

    @Test
    void findOrdersByPaymentMethod() {
        Cash cash = new Cash();
        cash.setAmount(BigDecimal.valueOf(100));
        cash.setCashTendered(BigDecimal.valueOf(100));
        paymentRepository.save(cash);

        Address address = new Address();
        address.setStreet("aboba");
        address.setCity("aboba");
        address.setZipcode("aboba");

        Customer customer = new Customer();
        customer.setName("aboba");
        customer.setAddress(address);
        customerRepository.save(customer);

        Order order = new Order();
        order.setDate(java.time.LocalDateTime.now());
        order.setStatus("aboba");
        order.setPayment(cash);
        order.setCustomer(customer);
        orderRepository.save(order);

        List<Order> orders = orderService.findOrdersByPaymentMethod(Cash.class);

        assertEquals(1, orders.size());
        assertInstanceOf(Cash.class, orders.get(0).getPayment());
    }

    @Test
    void findOrdersByCustomerName() {
        Cash cash = new Cash();
        cash.setAmount(BigDecimal.valueOf(100));
        cash.setCashTendered(BigDecimal.valueOf(100));
        paymentRepository.save(cash);

        Address address = new Address();
        address.setStreet("aboba");
        address.setCity("aboba");
        address.setZipcode("aboba");

        Customer customer = new Customer();
        customer.setName("aboba");
        customer.setAddress(address);
        customerRepository.save(customer);

        Order order = new Order();
        order.setDate(java.time.LocalDateTime.now());
        order.setStatus("aboba");
        order.setPayment(cash);
        order.setCustomer(customer);
        orderRepository.save(order);

        List<Order> orders = orderService.findOrdersByCustomerName("aboba");

        assertEquals(1, orders.size());
        assertEquals("aboba", orders.get(0).getCustomer().getName());
    }

    @Test
    void findOrdersByStatus() {
        Cash cash = new Cash();
        cash.setAmount(BigDecimal.valueOf(100));
        cash.setCashTendered(BigDecimal.valueOf(100));
        paymentRepository.save(cash);

        Address address = new Address();
        address.setStreet("aboba");
        address.setCity("aboba");
        address.setZipcode("aboba");

        Customer customer = new Customer();
        customer.setName("aboba");
        customer.setAddress(address);
        customerRepository.save(customer);

        Order order = new Order();
        order.setDate(java.time.LocalDateTime.now());
        order.setStatus("aboba");
        order.setPayment(cash);
        order.setCustomer(customer);
        orderRepository.save(order);

        List<Order> orders = orderService.findOrdersByStatus("aboba");

        assertEquals(1, orders.size());
        assertEquals("aboba", orders.get(0).getStatus());
    }
}
