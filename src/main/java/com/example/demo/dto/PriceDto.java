package com.example.demo.dto;

import lombok.*;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
public class PriceDto {
    Long id;
    String Chain_name;

    public Long getId() {
        return id;
    }


    public String getChain_name() {
        return Chain_name;
    }

    public void setChain_name(String chain_name) {
        Chain_name = chain_name;
    }

    public Integer getMaterial_No() {
        return Material_No;
    }

    public void setMaterial_No(Integer material_No) {
        Material_No = material_No;
    }

    public BigDecimal getRegular_price_per_unit() {
        return Regular_price_per_unit;
    }

    public void setRegular_price_per_unit(BigDecimal regular_price_per_unit) {
        Regular_price_per_unit = regular_price_per_unit;
    }

    Integer Material_No;
    BigDecimal Regular_price_per_unit;
}
