package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer CH3_Ship_To_Code;
    String CH3_Ship_To_Name;
    String Chain_name;

    public Long getId() {
        return id;
    }

    public Integer getCH3_Ship_To_Code() {
        return CH3_Ship_To_Code;
    }

    public void setCH3_Ship_To_Code(Integer CH3_Ship_To_Code) {
        this.CH3_Ship_To_Code = CH3_Ship_To_Code;
    }

    public String getCH3_Ship_To_Name() {
        return CH3_Ship_To_Name;
    }

    public void setCH3_Ship_To_Name(String CH3_Ship_To_Name) {
        this.CH3_Ship_To_Name = CH3_Ship_To_Name;
    }

    public String getChain_name() {
        return Chain_name;
    }

    public void setChain_name(String chain_name) {
        Chain_name = chain_name;
    }
}
