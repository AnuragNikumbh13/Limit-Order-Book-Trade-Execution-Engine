package com.anurag.hft.controller;

import com.anurag.hft.dto.OrderRequestDTO;
import com.anurag.hft.entity.Order;
import com.anurag.hft.repository.OrderRepository;
import com.anurag.hft.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;

    }

    @PostMapping
    public void placeOrder(
            @RequestBody OrderRequestDTO request
    ) {
        orderService.placeOrder(request);
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }
}
