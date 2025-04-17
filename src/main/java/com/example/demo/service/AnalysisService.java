package com.example.demo.service;

import com.example.demo.entity.Actual;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Product;
import com.example.demo.repository.ActualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    @Autowired
    private ActualRepository actualRepository;

    /**
     * Получение данных о продажах с разделением по признаку промо
     */
    public Map<String, Object> getSalesByPromoFlag(List<String> chainNames, List<String> productNames) {
        List<Actual> actuals = actualRepository.findByChainNamesAndProductNames(chainNames, productNames);

        return processSalesData(actuals);
    }

    /**
     * Получение данных о продажах по месяцам
     */
    public Map<String, Object> getMonthlySales(List<String> chainNames, List<String> productNames, List<String> categories) {
        List<Actual> actuals = actualRepository.findByChainNamesAndProductNamesAndCategories(chainNames, productNames, categories);

        Map<String, Object> result = new HashMap<>();

        // Группировка по месяцам
        Map<String, List<Actual>> salesByMonth = actuals.stream()
                .collect(Collectors.groupingBy(a -> {
                    YearMonth yearMonth = YearMonth.from(a.getDate().toLocalDate());
                    return yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                }));

        // Обработка данных по каждому месяцу
        Map<String, Map<String, Object>> monthlyData = new HashMap<>();

        for (Map.Entry<String, List<Actual>> entry : salesByMonth.entrySet()) {
            String month = entry.getKey();
            List<Actual> monthSales = entry.getValue();

            monthlyData.put(month, processSalesData(monthSales));
        }

        result.put("monthlySales", monthlyData);

        // Добавление агрегированных данных за весь период
        Map<String, Object> totalData = processSalesData(actuals);
        result.put("totalSales", totalData);

        return result;
    }

    /**
     * Получение данных о продажах за указанный период с разбивкой по дням
     */
    public Map<String, Object> getSalesByDateRange(Date startDate, Date endDate, List<String> chainNames, List<String> productNames) {
        List<Actual> actuals = actualRepository.findByDateRangeAndChainsAndProducts(startDate, endDate, chainNames, productNames);

        Map<String, List<Actual>> salesByDay = actuals.stream()
                .collect(Collectors.groupingBy(a -> a.getDate().toString()));

        Map<String, Object> result = new HashMap<>();
        Map<String, Map<String, Object>> dailyData = new HashMap<>();

        for (Map.Entry<String, List<Actual>> entry : salesByDay.entrySet()) {
            String date = entry.getKey();
            List<Actual> daySales = entry.getValue();

            dailyData.put(date, processSalesData(daySales));
        }

        result.put("dailySales", dailyData);
        result.put("totalSales", processSalesData(actuals));

        return result;
    }

    /**
     * Получение доли продаж по промо в процентах
     */
    public Map<String, Object> getPromoSharePercentage(List<String> chainNames, List<String> productNames) {
        List<Actual> actuals = actualRepository.findByChainNamesAndProductNames(chainNames, productNames);

        Map<String, Object> result = new HashMap<>();

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

        result.put("promoShareValuePercent", promoShareValue);
        result.put("promoShareUnitsPercent", promoShareUnits);

        return result;
    }

    /**
     * Вспомогательный метод для обработки данных о продажах
     */
    private Map<String, Object> processSalesData(List<Actual> sales) {
        Map<String, Object> result = new HashMap<>();

        List<Actual> regularSales = sales.stream()
                .filter(a -> "Regular".equals(a.getPromoFlag()))
                .collect(Collectors.toList());

        List<Actual> promoSales = sales.stream()
                .filter(a -> "Promo".equals(a.getPromoFlag()))
                .collect(Collectors.toList());

        // Расчет общих показателей
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

        // Расчет долей промо в процентах
        BigDecimal promoShareValue = totalValue.compareTo(BigDecimal.ZERO) > 0
                ? totalPromoValue.divide(totalValue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                : BigDecimal.ZERO;

        BigDecimal promoShareUnits = totalUnits > 0
                ? new BigDecimal(totalPromoUnits).divide(new BigDecimal(totalUnits), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                : BigDecimal.ZERO;

        // Сбор справочных полей
        Set<String> chains = sales.stream()
                .map(a -> a.getCustomer().getChain_name())
                .collect(Collectors.toSet());

        Set<String> categories = sales.stream()
                .map(a -> a.getProduct().getL3_Product_Category_Name())
                .collect(Collectors.toSet());

        Set<String> products = sales.stream()
                .map(a -> a.getProduct().getMaterial_Desc_RUS())
                .collect(Collectors.toSet());

        // Формирование результата
        result.put("chains", chains);
        result.put("categories", categories);
        result.put("products", products);
        result.put("regularSalesUnits", totalRegularUnits);
        result.put("promoSalesUnits", totalPromoUnits);
        result.put("totalUnits", totalUnits);
        result.put("regularSalesValue", totalRegularValue);
        result.put("promoSalesValue", totalPromoValue);
        result.put("totalValue", totalValue);
        result.put("promoShareValuePercent", promoShareValue);
        result.put("promoShareUnitsPercent", promoShareUnits);

        return result;
    }
}