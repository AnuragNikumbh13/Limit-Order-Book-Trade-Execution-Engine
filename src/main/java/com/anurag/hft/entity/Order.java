package com.anurag.hft.entity;

import com.anurag.hft.enums.OrderStatus;
import com.anurag.hft.enums.OrderType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;
    private String symbol;
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    private BigDecimal quantity;
    private BigDecimal remainingQuantity;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @CreationTimestamp
    private Timestamp createdAt;


    public Order(){

    }

    public Order(User user, String symbol, OrderType orderType, BigDecimal quantity, BigDecimal remainingQuantity, BigDecimal price, OrderStatus orderStatus) {
        this.user = user;
        this.symbol = symbol;
        this.orderType = orderType;
        this.quantity = quantity;
        this.remainingQuantity = remainingQuantity;
        this.price = price;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(BigDecimal remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", symbol='" + symbol + '\'' +
                ", orderType=" + orderType +
                ", quantity=" + quantity +
                ", remainingQuantity=" + remainingQuantity +
                ", price=" + price +
                ", orderStatus=" + orderStatus +
                ", createdAt=" + createdAt +
                '}';
    }
}
