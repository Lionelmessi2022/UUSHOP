package com.test.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class SellerProductFindVo {
    private  boolean status;
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String icon;
    private Map category;
}
