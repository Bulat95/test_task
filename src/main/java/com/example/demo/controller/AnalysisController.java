package com.example.demo.controller;

import com.example.demo.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @GetMapping("/sales")
    public Map<String, Object> getSalesByPromoFlag(
            @RequestParam(required = false) List<String> chainNames,
            @RequestParam(required = false) List<String> productNames) {
        return analysisService.getSalesByPromoFlag(chainNames, productNames);
    }

    @GetMapping("/sales/byDate")
    public Map<String, Object> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(required = false) List<String> chainNames,
            @RequestParam(required = false) List<String> productNames) {
        return analysisService.getSalesByDateRange(startDate, endDate, chainNames, productNames);
    }
}