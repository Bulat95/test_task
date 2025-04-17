package com.example.demo.repository;

import com.example.demo.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    @Query("SELECT p FROM Price p WHERE p.Chain_name = :chainName AND p.Material_No = :materialNo")
    Price findByChainAndMaterial(
            @Param("chainName") String chainName,
            @Param("materialNo") Integer materialNo);
}
