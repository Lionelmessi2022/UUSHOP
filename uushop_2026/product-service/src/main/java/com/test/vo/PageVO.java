package com.test.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO {
    private List<SellerProductVO> content;
    private long size;
    private long total;


}
