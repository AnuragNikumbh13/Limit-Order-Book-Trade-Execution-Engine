package com.anurag.hft.controller;

import com.anurag.hft.entity.Trade;
import com.anurag.hft.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trades")
public class TradeController {
    @Autowired
    private TradeService tradeService;

    @GetMapping
    public List<Trade> getAllTrades(){
        return tradeService.getAllTrades();
    }
    @GetMapping("/{id}")
    public Trade getTradeById(@PathVariable Long id){
        return tradeService.getTradeById(id);
    }

}
