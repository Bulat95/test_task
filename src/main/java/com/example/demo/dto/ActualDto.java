package com.example.demo.dto;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;


@AllArgsConstructor
@NoArgsConstructor
public class ActualDto {

    private Long id;

    private Date date;

    private Product product;

    public Long getId() {
        return id;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getMaterial_No() {
        return Material_No;
    }

    public void setMaterial_No(Integer material_No) {
        Material_No = material_No;
    }

    public Integer getCH3_Ship_To_Code() {
        return CH3_Ship_To_Code;
    }

    public void setCH3_Ship_To_Code(Integer CH3_Ship_To_Code) {
        this.CH3_Ship_To_Code = CH3_Ship_To_Code;
    }

    public Integer getVolume_units() {
        return Volume_units;
    }

    public void setVolume_units(Integer volume_units) {
        Volume_units = volume_units;
    }

    public BigDecimal getActual_Sales_Value() {
        return Actual_Sales_Value;
    }

    public void setActual_Sales_Value(BigDecimal actual_Sales_Value) {
        Actual_Sales_Value = actual_Sales_Value;
    }

    public String getPromoFlag() {
        return promoFlag;
    }

    public void setPromoFlag(String promoFlag) {
        this.promoFlag = promoFlag;
    }

    private Customer customer;
    private Integer Material_No;
    private Integer CH3_Ship_To_Code;
    private Integer Volume_units;
    private BigDecimal Actual_Sales_Value;
    private String promoFlag;
}
