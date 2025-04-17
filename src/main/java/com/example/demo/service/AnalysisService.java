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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    @Autowired
    private ActualRepository actualRepository;

    public Map<String, Object> getSalesByPromoFlag(List<String> chainNames, List<String> productNames) {
        List<Actual> actuals = actualRepository.findByChainNamesAndProductNames(chainNames, productNames);

        List<Actual> regularSales = actuals.stream()
                .filter(a -> "Regular".equals(a.getPromoFlag()))
                .collect(Collectors.toList());

        List<Actual> promoSales = actuals.stream()
                .filter(a -> "Promo".equals(a.getPromoFlag()))
                .collect(Collectors.toList());

        Integer totalRegularUnits = regularSales.stream().mapToInt(Actual::getVolume_units).sum();
        Integer totalPromoUnits = promoSales.stream().mapToInt(Actual::getVolume_units).sum();
        Integer totalUnits = totalRegularUnits + totalPromoUnits;

        BigDecimal totalRegularValue = regularSales.stream()
                .map(Actual::getActual_Sales_Value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPromoValue = promoSales.stream()
                .map(Actual::getActual_Sales_Value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalValue = totalRegularValue.add(totalPromoValue);

        BigDecimal promoShareValue = totalValue.compareTo(BigDecimal.ZERO) > 0
                ? totalPromoValue.divide(totalValue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                : BigDecimal.ZERO;

        BigDecimal promoShareUnits = totalUnits > 0
                ? new BigDecimal(totalPromoUnits).divide(new BigDecimal(totalUnits), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                : BigDecimal.ZERO;

        Map<String, Object> result = new HashMap<>();

        // Собираем справочные поля
        Set<String> chains = actuals.stream()
                .map(a -> a.getCustomer().getChain_name())
                .collect(Collectors.toSet());

        Set<String> categories = actuals.stream()
                .map(a -> a.getProduct().getL3_Product_Category_Name())
                .collect(Collectors.toSet());

        // Собираем информацию по месяцам
        Map<String, Map<String, Object>> salesByMonth = actuals.stream()
                .collect(Collectors.groupingBy(a -> {
                    YearMonth yearMonth = YearMonth.from(a.getDate().toLocalDate());
                    return yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                }))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<Actual> monthSales = entry.getValue();
                            Map<String, Object> monthData = new HashMap<>();

                            List<Actual> monthRegularSales = monthSales.stream()
                                    .filter(a -> "Regular".equals(a.getPromoFlag()))
                                    .collect(Collectors.toList());
                            List<Actual> monthPromoSales = monthSales.stream()
                                    .filter(a -> "Promo".equals(a.getPromoFlag()))
                                    .collect(Collectors.toList());

                            Integer monthRegularUnits = monthRegularSales.stream().mapToInt(Actual::getVolume_units).sum();
                            Integer monthPromoUnits = monthPromoSales.stream().mapToInt(Actual::getVolume_units).sum();

                            BigDecimal monthRegularValue = monthRegularSales.stream()
                                    .map(Actual::getActual_Sales_Value)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            BigDecimal monthPromoValue = monthPromoSales.stream()
                                    .map(Actual::getActual_Sales_Value)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            monthData.put("regularUnits", monthRegularUnits);
                            monthData.put("promoUnits", monthPromoUnits);
                            monthData.put("regularValue", monthRegularValue);
                            monthData.put("promoValue", monthPromoValue);

                            return monthData;
                        }
                ));

        // Добавляем все данные в результат
        result.put("chains", chains);
        result.put("categories", categories);
        result.put("regularSalesUnits", totalRegularUnits);
        result.put("promoSalesUnits", totalPromoUnits);
        result.put("regularSalesValue", totalRegularValue);
        result.put("promoSalesValue", totalPromoValue);
        result.put("promoShareValuePercent", promoShareValue);
        result.put("promoShareUnitsPercent", promoShareUnits);
        result.put("salesByMonth", salesByMonth);

        return result;
    }

    public Map<String, Object> getSalesByDateRange(Date startDate, Date endDate, List<String> chainNames, List<String> productNames) {
        List<Actual> actuals = actualRepository.findByDateRangeAndChainsAndProducts(startDate, endDate, chainNames, productNames);

        Map<String, List<Actual>> salesByDay = actuals.stream()
                .collect(Collectors.groupingBy(a -> a.getDate().toString()));

        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<String, List<Actual>> entry : salesByDay.entrySet()) {
            Map<String, Object> dayData = new HashMap<>();
            List<Actual> daySales = entry.getValue();

            List<Actual> regularSales = daySales.stream()
                    .filter(a -> "Regular".equals(a.getPromoFlag()))
                    .collect(Collectors.toList());

            List<Actual> promoSales = daySales.stream()
                    .filter(a -> "Promo".equals(a.getPromoFlag()))
                    .collect(Collectors.toList());

            dayData.put("regularSalesUnits", regularSales.stream().mapToInt(Actual::getVolume_units).sum());
            dayData.put("promoSalesUnits", promoSales.stream().mapToInt(Actual::getVolume_units).sum());
            dayData.put("regularSalesValue", regularSales.stream()
                    .map(Actual::getActual_Sales_Value)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            dayData.put("promoSalesValue", promoSales.stream()
                    .map(Actual::getActual_Sales_Value)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            result.put(entry.getKey(), dayData);
        }

        return result;
    }
}