package com.example.demo.controller;

import com.example.demo.entity.Price;
import com.example.demo.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private PriceRepository priceRepository;

    @GetMapping("/prices")
    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    @GetMapping("/prices/{id}")
    public ResponseEntity<Price> getPriceById(@PathVariable Long id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found with id: " + id));
        return ResponseEntity.ok(price);
    }

    @PostMapping("/prices")
    public Price createPrice(@RequestBody Price price) {
        return priceRepository.save(price);
    }

    @PutMapping("/prices/{id}")
    public ResponseEntity<Price> updatePrice(@PathVariable Long id, @RequestBody Price priceDetails) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found with id: " + id));

        price.setChain_name(priceDetails.getChain_name());
        price.setMaterial_No(priceDetails.getMaterial_No());
        price.setRegular_price_per_unit(priceDetails.getRegular_price_per_unit());

        Price updatedPrice = priceRepository.save(price);
        return ResponseEntity.ok(updatedPrice);
    }

    @DeleteMapping("/prices/{id}")
    public ResponseEntity<?> deletePrice(@PathVariable Long id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found with id: " + id));

        priceRepository.delete(price);
        return ResponseEntity.ok().build();
    }
}
