package com.example.demo.service;

import com.example.demo.dto.ActualDto;
import com.example.demo.entity.Actual;
import com.example.demo.mapper.ReflectionMapper;
import com.example.demo.repository.ActualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    @Autowired
    private ActualRepository actualRepository;

    public Map<String, Object> getSalesByPromoFlag(List<String> chainNames, List<String> productNames) {
        List<ActualDto> actuals = actualRepository.findByChainNamesAndProductNames(chainNames, productNames)
                .stream()
                .map(x -> ReflectionMapper.toDto(x, ActualDto.class)).toList();

        List<ActualDto> regularSales = actuals.stream()
                .map(x -> ReflectionMapper.toDto(x, ActualDto.class))
                .filter(a -> "Regular".equals(a.getPromoFlag()))
                .toList();

        List<ActualDto> promoSales = actuals.stream()
                .map(x -> ReflectionMapper.toDto(x, ActualDto.class))
                .filter(a -> "Promo".equals(a.getPromoFlag()))
                .toList();

        Integer totalRegularUnits = regularSales.stream().mapToInt(ActualDto::getVolume_units).sum();
        Integer totalPromoUnits = promoSales.stream().mapToInt(ActualDto::getVolume_units).sum();
        Integer totalUnits = totalRegularUnits + totalPromoUnits;

        BigDecimal totalRegularValue = regularSales.stream()
                .map(ActualDto::getActual_Sales_Value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPromoValue = promoSales.stream()
                .map(ActualDto::getActual_Sales_Value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalValue = totalRegularValue.add(totalPromoValue);

        BigDecimal promoShareValue = totalValue.compareTo(BigDecimal.ZERO) > 0
                ? totalPromoValue.divide(totalValue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                : BigDecimal.ZERO;

        BigDecimal promoShareUnits = totalUnits > 0
                ? new BigDecimal(totalPromoUnits).divide(new BigDecimal(totalUnits), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                : BigDecimal.ZERO;

        Map<String, Object> result = new HashMap<>();

        Set<String> chains = actuals.stream()
                .map(a -> a.getCustomer().getChain_name())
                .collect(Collectors.toSet());

        Set<String> categories = actuals.stream()
                .map(a -> a.getProduct().getL3_Product_Category_Name())
                .collect(Collectors.toSet());

        result.put("chains", chains);
        result.put("categories", categories);
        result.put("regularSalesUnits", totalRegularUnits);
        result.put("promoSalesUnits", totalPromoUnits);
        result.put("regularSalesValue", totalRegularValue);
        result.put("promoSalesValue", totalPromoValue);
        result.put("promoShareValue", promoShareValue);
        result.put("promoShareUnits", promoShareUnits);

        return result;
    }

    public Map<String, Object> getSalesByDateRange(Date startDate, Date endDate) {
        List<Actual> actuals = actualRepository.findByDateRange(startDate, endDate);

        Map<YearMonth, List<Actual>> salesByMonth = actuals.stream()
                .collect(Collectors.groupingBy(a ->
                        YearMonth.from(a.getDate().toLocalDate())));

        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<YearMonth, List<Actual>> entry : salesByMonth.entrySet()) {
            Map<String, Object> monthData = new HashMap<>();
            List<Actual> monthSales = entry.getValue();

            List<Actual> regularSales = monthSales.stream()
                    .filter(a -> "Regular".equals(a.getPromoFlag()))
                    .collect(Collectors.toList());

            List<Actual> promoSales = monthSales.stream()
                    .filter(a -> "Promo".equals(a.getPromoFlag()))
                    .collect(Collectors.toList());
            
            monthData.put("regularSalesUnits", regularSales.stream().mapToInt(Actual::getVolume_units).sum());
            monthData.put("promoSalesUnits", promoSales.stream().mapToInt(Actual::getVolume_units).sum());
            monthData.put("regularSalesValue", regularSales.stream()
                    .map(Actual::getActual_Sales_Value)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            monthData.put("promoSalesValue", promoSales.stream()
                    .map(Actual::getActual_Sales_Value)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            result.put(entry.getKey().toString(), monthData);
        }

        return result;
    }
}