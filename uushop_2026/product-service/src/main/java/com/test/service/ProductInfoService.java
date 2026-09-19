package com.test.service;

import com.test.entity.ProductInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author test
 * @since 2026-08-13
 */
public interface ProductInfoService extends IService<ProductInfo> {
    public List<ProductInfo> excleToProductInfoList(InputStream inputStream);
}