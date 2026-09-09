package com.anurag.hft.service;
import com.anurag.hft.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.anurag.hft.dto.OrderRequestDTO;
import com.anurag.hft.entity.Order;
import com.anurag.hft.enums.OrderStatus;
import com.anurag.hft.repository.OrderRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMatchingService orderMatchingService;

    public OrderService(OrderRepository orderRepository, OrderMatchingService orderMatchingService) {
        this.orderRepository = orderRepository;
        this.orderMatchingService = orderMatchingService;
    }

    @Transactional
    public void placeOrder(OrderRequestDTO request) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        Order order = new Order(
                user,
                request.getSymbol(),
                request.getOrderType(),
                request.getQuantity(),
                request.getQuantity(),
                request.getPrice(),
                OrderStatus.ORDER_PLACED
        );

        Order savedOrder = orderRepository.save(order);
        orderMatchingService.matchOrder(savedOrder);
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUser_Id(userId);
    }

}
