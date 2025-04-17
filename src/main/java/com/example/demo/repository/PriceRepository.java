package com.example.demo.repository;

import com.example.demo.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    Price findByChain_nameAndMaterial_No(String chainName, Integer materialNo);
}
