package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
public class Actual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "Material_No", referencedColumnName = "Material_No", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "CH3_Ship_To_Code", referencedColumnName = "CH3_Ship_To_Code", insertable = false, updatable = false)
    private Customer customer;

    private Integer Material_No;
    private Integer CH3_Ship_To_Code;
    private Integer Volume_units;
    private BigDecimal Actual_Sales_Value;

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

    private String promoFlag;
}
