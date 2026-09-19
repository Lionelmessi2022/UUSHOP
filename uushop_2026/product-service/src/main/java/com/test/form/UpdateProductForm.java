package com.test.form;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class UpdateProductForm {
    private boolean status;
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String icon;
    private UpdateProductInnerForm category;
}
