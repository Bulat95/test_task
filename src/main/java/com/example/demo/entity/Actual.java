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
    @JoinColumn(name = "Material_No", referencedColumnName = "Material_No")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "CH3_Ship_To_Code", referencedColumnName = "CH3_Ship_To_Code")
    private Customer customer;

    private Integer Material_No;
    private Integer CH3_Ship_To_Code;
    private Integer Volume_units;
    private BigDecimal Actual_Sales_Value;
    private String promoFlag;
}
