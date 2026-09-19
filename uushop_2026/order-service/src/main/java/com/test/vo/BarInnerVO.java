package com.test.vo;

import lombok.Data;

import java.util.Map;

@Data
public class BarInnerVO {
    private Integer value;
    private Map<String,String> ItemStyle;
}
