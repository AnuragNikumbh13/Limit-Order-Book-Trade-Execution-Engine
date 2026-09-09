package com.anurag.hft.repository;

import com.anurag.hft.entity.Order;
import com.anurag.hft.enums.OrderStatus;
import com.anurag.hft.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findBySymbolAndOrderTypeAndOrderStatusInOrderByPriceAsc(
            String symbol,
            OrderType orderType,
            List<OrderStatus> orderStatuses
    );

   List<Order> findBySymbolAndOrderTypeAndOrderStatusInOrderByPriceDesc(
            String symbol,
            OrderType orderType,
            List<OrderStatus> orderStatuses
    );
    List<Order> findByUser_Id(Long userId);
}

