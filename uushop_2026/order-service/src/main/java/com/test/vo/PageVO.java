package com.test.vo;

import com.test.entity.OrderMaster;
import lombok.Data;

import java.util.List;

@Data
public class PageVO {
    private List<OrderMaster> content;
    private Long size;
    private Long total;
}
