package com.anurag.lob.service;

import com.anurag.lob.entity.Order;
import com.anurag.lob.entity.Trade;
import com.anurag.lob.enums.OrderStatus;
import com.anurag.lob.enums.OrderType;
import com.anurag.lob.repository.OrderRepository;
import com.anurag.lob.repository.TradeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class    OrderMatchingService {

    private final OrderRepository orderRepository;

    private final TradeRepository tradeRepository;

    public OrderMatchingService(OrderRepository orderRepository, TradeRepository tradeRepository) {
        this.orderRepository = orderRepository;
        this.tradeRepository = tradeRepository;
    }

    @Transactional
    public void matchOrder(Order incomingOrder)
    {

        incomingOrder.setOrderStatus(OrderStatus.OPEN);

        OrderType oppositeOrderType =
                incomingOrder.getOrderType() == OrderType.BUY
                        ? OrderType.SELL // ? stands for true - return OrderType.SELL
                        : OrderType.BUY; // : stands for false - returns OrderType.BUY

        List<OrderStatus> activeStatuses = List.of(
                OrderStatus.OPEN,
                OrderStatus.PARTIALLY_FILLED
        );

        List<Order> oppositeOrders;
        if(incomingOrder.getOrderType() == OrderType.BUY) {
            oppositeOrders = orderRepository
                    .findBySymbolAndOrderTypeAndOrderStatusInOrderByPriceAscCreatedAtAscIdAsc(
                            incomingOrder.getSymbol(),
                            oppositeOrderType,
                            activeStatuses
                    );
        }

        else{
            oppositeOrders = orderRepository
                    .findBySymbolAndOrderTypeAndOrderStatusInOrderByPriceDescCreatedAtAscIdAsc(
                            incomingOrder.getSymbol(),
                            oppositeOrderType,
                            activeStatuses
                    );
            }


        for(Order existingOrder : oppositeOrders ){
            boolean priceMatches=
                    incomingOrder.getOrderType() == OrderType.BUY
                    ? incomingOrder.getPrice().compareTo(existingOrder.getPrice())>=0
                    :incomingOrder.getPrice().compareTo(existingOrder.getPrice()) <=0;

            if (!priceMatches) {
                break;
            }

            BigDecimal matchedQuantity = incomingOrder.getRemainingQuantity()
                    .compareTo(existingOrder.getRemainingQuantity()) <= 0
                    ? incomingOrder.getRemainingQuantity()
                    : existingOrder.getRemainingQuantity();

            incomingOrder.setRemainingQuantity(incomingOrder.getRemainingQuantity().subtract(matchedQuantity));
            existingOrder.setRemainingQuantity(existingOrder.getRemainingQuantity().subtract(matchedQuantity));

            if(incomingOrder.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0){
                incomingOrder.setOrderStatus(OrderStatus.FILLED);
            } else {
                incomingOrder.setOrderStatus(OrderStatus.PARTIALLY_FILLED);
            }

            if(existingOrder.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0){
                existingOrder.setOrderStatus(OrderStatus.FILLED);
            } else {
                existingOrder.setOrderStatus(OrderStatus.PARTIALLY_FILLED);
            }

            Order buyOrder;
            Order sellOrder;
            if(incomingOrder.getOrderType()==OrderType.BUY){
                buyOrder=incomingOrder;
                sellOrder=existingOrder;
            }
           else {
                buyOrder=existingOrder;
                sellOrder=incomingOrder;
            }

           Trade trade=new Trade();
           trade.setSymbol(incomingOrder.getSymbol());
           trade.setQuantity(matchedQuantity);
           trade.setPrice(existingOrder.getPrice());
           trade.setBuyOrder(buyOrder);
           trade.setSellOrder(sellOrder);
           tradeRepository.save(trade);


            orderRepository.save(incomingOrder);
            orderRepository.save(existingOrder);

            if (incomingOrder.getOrderStatus() == OrderStatus.FILLED) {
                break;
            }
        }

        orderRepository.save(incomingOrder);

    }
}




