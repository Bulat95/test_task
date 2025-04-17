package com.example.demo.service;

import com.example.demo.entity.Price;
import com.example.demo.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceService {

    @Autowired
    private PriceRepository priceRepository;

    /**
     * Получение всех цен
     */
    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    /**
     * Получение цены по ID
     */
    public Price getPriceById(Long id) {
        return priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Цена с ID " + id + " не найдена"));
    }

    /**
     * Получение цен по названию сети
     */
    public List<Price> getPricesByChain(String chainName) {
        return priceRepository.findByChainName(chainName);
    }

    /**
     * Получение цены по названию сети и коду продукта
     */
    public Price getPriceByChainAndMaterial(String chainName, Integer materialNo) {
        Price price = priceRepository.findByChainAndMaterial(chainName, materialNo);
        if (price == null) {
            throw new RuntimeException("Цена для сети " + chainName + " и материала " + materialNo + " не найдена");
        }
        return price;
    }

    /**
     * Создание новой цены
     */
    public Price createPrice(Price price) {
        // Проверка существования цены с такими же параметрами
        Price existingPrice = priceRepository.findByChainAndMaterial(
                price.getChain_name(), price.getMaterial_No());

        if (existingPrice != null) {
            throw new RuntimeException("Цена для сети " + price.getChain_name() +
                    " и материала " + price.getMaterial_No() + " уже существует");
        }

        return priceRepository.save(price);
    }

    /**
     * Обновление существующей цены
     */
    public Price updatePrice(Long id, Price priceDetails) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Цена с ID " + id + " не найдена"));

        price.setChain_name(priceDetails.getChain_name());
        price.setMaterial_No(priceDetails.getMaterial_No());
        price.setRegular_price_per_unit(priceDetails.getRegular_price_per_unit());

        return priceRepository.save(price);
    }

    /**
     * Удаление цены
     */
    public Map<String, Boolean> deletePrice(Long id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Цена с ID " + id + " не найдена"));

        priceRepository.delete(price);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}