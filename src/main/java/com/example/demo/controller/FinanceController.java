package com.example.demo.controller;

import com.example.demo.entity.Price;
import com.example.demo.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @GetMapping("/prices")
    public ResponseEntity<List<Price>> getAllPrices() {
        List<Price> prices = financeService.getAllPrices();
        return ResponseEntity.ok(prices);
    }

    /**
     * Получение цены по идентификатору
     */
    @GetMapping("/prices/{id}")
    public ResponseEntity<Price> getPriceById(@PathVariable Long id) {
        Price price = financeService.getPriceById(id);
        return ResponseEntity.ok(price);
    }

    /**
     * Получение цен по названию сети
     */
    @GetMapping("/prices/chain/{chainName}")
    public ResponseEntity<List<Price>> getPricesByChain(@PathVariable String chainName) {
        List<Price> prices = financeService.getPricesByChain(chainName);
        return ResponseEntity.ok(prices);
    }

    /**
     * Получение цены по названию сети и коду продукта
     */
    @GetMapping("/prices/chain-material")
    public ResponseEntity<Price> getPriceByChainAndMaterial(
            @RequestParam String chainName,
            @RequestParam Integer materialNo) {
        Price price = financeService.getPriceByChainAndMaterial(chainName, materialNo);
        return ResponseEntity.ok(price);
    }

    /**
     * Создание новой цены
     */
    @PostMapping("/prices")
    public ResponseEntity<Price> createPrice(@RequestBody Price price) {
        Price createdPrice = financeService.createPrice(price);
        return new ResponseEntity<>(createdPrice, HttpStatus.CREATED);
    }

    /**
     * Обновление существующей цены
     */
    @PutMapping("/prices/{id}")
    public ResponseEntity<Price> updatePrice(@PathVariable Long id, @RequestBody Price priceDetails) {
        Price updatedPrice = financeService.updatePrice(id, priceDetails);
        return ResponseEntity.ok(updatedPrice);
    }

    /**
     * Удаление цены
     */
    @DeleteMapping("/prices/{id}")
    public ResponseEntity<Map<String, Boolean>> deletePrice(@PathVariable Long id) {
        Map<String, Boolean> response = financeService.deletePrice(id);
        return ResponseEntity.ok(response);
    }
}