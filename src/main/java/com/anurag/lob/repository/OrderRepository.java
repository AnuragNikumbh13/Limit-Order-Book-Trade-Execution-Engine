package com.anurag.lob.repository;

import com.anurag.lob.entity.Order;
import com.anurag.lob.enums.OrderStatus;
import com.anurag.lob.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findBySymbolAndOrderTypeAndOrderStatusInOrderByPriceAscCreatedAtAscIdAsc(
            String symbol,
            OrderType orderType,
            List<OrderStatus> orderStatuses
    );

    List<Order> findBySymbolAndOrderTypeAndOrderStatusInOrderByPriceDescCreatedAtAscIdAsc(
            String symbol,
            OrderType orderType,
            List<OrderStatus> orderStatuses
    );
    List<Order> findByUser_Id(Long userId);
}

