package com.test.vo;

import com.test.entity.Admin;
import lombok.Data;

@Data
public class AdminLoginVO extends Admin {
    private String token;
}
