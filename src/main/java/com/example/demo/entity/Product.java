package com.example.demo.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    Integer Material_No;
    String Material_Desc_RUS;

    public Long getId() {
        return id;
    }


    public Integer getMaterial_No() {
        return Material_No;
    }

    public void setMaterial_No(Integer material_No) {
        Material_No = material_No;
    }

    public String getMaterial_Desc_RUS() {
        return Material_Desc_RUS;
    }

    public void setMaterial_Desc_RUS(String material_Desc_RUS) {
        Material_Desc_RUS = material_Desc_RUS;
    }

    public Integer getL3_Product_Category_Code() {
        return L3_Product_Category_Code;
    }

    public void setL3_Product_Category_Code(Integer l3_Product_Category_Code) {
        L3_Product_Category_Code = l3_Product_Category_Code;
    }

    public String getL3_Product_Category_Name() {
        return L3_Product_Category_Name;
    }

    public void setL3_Product_Category_Name(String l3_Product_Category_Name) {
        L3_Product_Category_Name = l3_Product_Category_Name;
    }

    Integer L3_Product_Category_Code;
    String L3_Product_Category_Name;

}
