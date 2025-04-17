package com.example.demo.service;

import com.example.demo.entity.Actual;
import com.example.demo.entity.Price;
import com.example.demo.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PromoFlagService {

    @Autowired
    private PriceRepository priceRepository;

    public String determinePromoFlag(Actual actual, String chainName, Integer materialNo) {

        Price price = priceRepository.findByChain_nameAndMaterial_No(chainName, materialNo);

        if (price == null) {
            return "Regular";
        }

        BigDecimal actualPricePerUnit = actual.getActual_Sales_Value()
                .divide(new BigDecimal(actual.getVolume_units()), 2, RoundingMode.HALF_UP);

        if (actualPricePerUnit.compareTo(price.getRegular_price_per_unit()) < 0) {
            return "Promo";
        } else {
            return "Regular";
        }
    }
}