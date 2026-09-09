package com.anurag.hft.service;

import com.anurag.hft.entity.Trade;
import com.anurag.hft.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {
    @Autowired
    private TradeRepository tradeRepository;

    public List<Trade> getAllTrades(){
        return tradeRepository.findAll();
    }

    public Trade getTradeById(Long id){
        return tradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trade Not Found"));
    }

}
