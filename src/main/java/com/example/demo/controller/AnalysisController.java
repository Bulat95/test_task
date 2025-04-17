package com.example.demo.controller;

import com.example.demo.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    /**
     * Метод для выгрузки фактов продаж с учётом признака промо
     * Возвращает все справочные поля: сеть, категория, месяц, факт продаж по базовой и промо ценам
     */
    @GetMapping("/sales")
    public ResponseEntity<Map<String, Object>> getSalesByPromoFlag(
            @RequestParam(required = false) List<String> chainNames,
            @RequestParam(required = false) List<String> productNames) {
        Map<String, Object> result = analysisService.getSalesByPromoFlag(chainNames, productNames);
        return ResponseEntity.ok(result);
    }

    /**
     * Метод для выборки данных по конкретным сетям и продуктам с группировкой по месяцам
     */
    @GetMapping("/sales/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlySales(
            @RequestParam(required = false) List<String> chainNames,
            @RequestParam(required = false) List<String> productNames,
            @RequestParam(required = false) List<String> categories) {
        Map<String, Object> result = analysisService.getMonthlySales(chainNames, productNames, categories);
        return ResponseEntity.ok(result);
    }

    /**
     * Метод для выгрузки фактов по дням согласно фильтрации по списку наименований сетей и списку продуктов
     */
    @GetMapping("/sales/daily")
    public ResponseEntity<Map<String, Object>> getDailySales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(required = false) List<String> chainNames,
            @RequestParam(required = false) List<String> productNames) {
        Map<String, Object> result = analysisService.getSalesByDateRange(startDate, endDate, chainNames, productNames);
        return ResponseEntity.ok(result);
    }

    /**
     * Метод для получения доли продаж по промо в процентах
     */
    @GetMapping("/sales/promo-share")
    public ResponseEntity<Map<String, Object>> getPromoSharePercentage(
            @RequestParam(required = false) List<String> chainNames,
            @RequestParam(required = false) List<String> productNames) {
        Map<String, Object> result = analysisService.getPromoSharePercentage(chainNames, productNames);
        return ResponseEntity.ok(result);
    }
}