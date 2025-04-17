package com.example.demo.repository;

import com.example.demo.entity.Actual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ActualRepository extends JpaRepository<Actual, Long> {

    @Query("SELECT a FROM Actual a " +
            "JOIN a.customer c " +
            "JOIN a.product p " +
            "WHERE (:chainNames IS NULL OR c.Chain_name IN :chainNames) " +
            "AND (:productNames IS NULL OR p.Material_Desc_RUS IN :productNames)")
    List<Actual> findByChainNamesAndProductNames(
            @Param("chainNames") List<String> chainNames,
            @Param("productNames") List<String> productNames);

    @Query("SELECT a FROM Actual a " +
            "WHERE a.date BETWEEN :startDate AND :endDate")
    List<Actual> findByDateRange(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}
