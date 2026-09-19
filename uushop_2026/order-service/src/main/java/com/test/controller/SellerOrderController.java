package com.test.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.com.test.vo.ResultVO;
import com.test.dto.BarDTO;
import com.test.dto.LineDTO;
import com.test.entity.OrderDetail;
import com.test.entity.OrderMaster;
import com.test.exception.ShopException;
import com.test.feign.ProductFeign;
import com.test.mapper.OrderDetailMapper;
import com.test.result.ResponseEnum;
import com.test.service.OrderDetailService;
import com.test.service.OrderMasterService;
import com.test.util.EChartsColorUtil;
import com.test.util.ResultVOUtil;
import com.test.vo.*;
import org.apache.commons.collections.OrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author test
 * @since 2026-08-16
 */
@RestController
@RequestMapping("/seller/order")
public class SellerOrderController {

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductFeign productFeign;

    @Autowired
    private OrderDetailMapper orderDetailMapper;




    @GetMapping("/list/{page}/{size}")
    public ResultVO list(
            @PathVariable("page") Integer page,
            @PathVariable("size") Integer size
    ){

        Page<OrderMaster> pageModel = new Page<>(page, size);
        QueryWrapper<OrderMaster> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("update_time");
        Page<OrderMaster> resultPage = orderMasterService.page(pageModel, queryWrapper);
        PageVO pageVO = new PageVO();
        pageVO.setContent(resultPage.getRecords());
        pageVO.setSize(resultPage.getSize());
        pageVO.setTotal(resultPage.getTotal());
        return ResultVOUtil.success(pageVO);



    }

    //取消订单

    @PutMapping("/cancel/{orderId}")
    public ResultVO cancel(@PathVariable("orderId") String orderId  ){
        OrderMaster orderMaster = this.orderMasterService.getById(orderId);
        if (orderMaster == null) throw new ShopException(ResponseEnum.ORDER_NULL.getMsg());
        if (orderMaster.getOrderStatus() == 1) throw new ShopException(ResponseEnum.ORDER_FINISH.getMsg());
        if (orderMaster.getOrderStatus() == 2) throw new ShopException(ResponseEnum.ORDER_CANCEL.getMsg());
        orderMaster.setOrderStatus(2);
        boolean updateById = this.orderMasterService.updateById(orderMaster);
        //还原库存
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        List<OrderDetail> list = this.orderDetailService.list(queryWrapper);
        for (OrderDetail orderDetail : list) {
            Integer productId = orderDetail.getProductId();
            Integer productQuantity = orderDetail.getProductQuantity();
            Boolean subStockById = this.productFeign.subStockById(productId, -productQuantity);

        }
        return ResultVOUtil.success(null);


    }

    //完结订单
    @PutMapping("/finish/{orderId}")

    public ResultVO finish(@PathVariable("orderId") String orderId  ){
        OrderMaster orderMaster = this.orderMasterService.getById(orderId);
        if (orderMaster == null) throw new ShopException(ResponseEnum.ORDER_NULL.getMsg());
        if (orderMaster.getOrderStatus() == 1) throw new ShopException(ResponseEnum.ORDER_FINISH_ERROR.getMsg());
        if (orderMaster.getOrderStatus() == 2) throw new ShopException(ResponseEnum.ORDER_CANCEL_ERROR.getMsg());
        if (orderMaster.getPayStatus() == 0) throw new ShopException(ResponseEnum.ORDER_NOT_PAY.getMsg());
        orderMaster.setOrderStatus(1);
        boolean updateById = this.orderMasterService.updateById(orderMaster);
        if (updateById) return ResultVOUtil.success(null);
        return ResultVOUtil.fail(ResponseEnum.ORDER_FINISH_ERROR.getMsg());


    }

    //柱状图
    @GetMapping("/barSale")
    public ResultVO barSale(){
        List<BarDTO> barDTOS = this.orderDetailMapper.barList();
        List<String> names = new ArrayList<>();
        List<BarInnerVO> values = new ArrayList<>();
        for(BarDTO barDTO : barDTOS) {
            String name = barDTO.getName();
            names.add(name);
            Integer value = barDTO.getValue();
            BarInnerVO vo = new BarInnerVO();
            vo.setValue(value);
            values.add(vo);
            Map<String, String> itemStyle = EChartsColorUtil.createItemStyle(value);
            vo.setItemStyle(itemStyle);

        }
        BarVO barVO = new BarVO();
        barVO.setNames(names);
        barVO.setValues(values);
        return ResultVOUtil.success(barVO);
    }

    //基础折线图（以天为单位统计当天的销量）
    @GetMapping("/basicLineSale")
    public ResultVO basicLineSale(){
        List<LineDTO> lineDTOS = this.orderDetailMapper.lineList();
        Map<String,List> map = new HashMap<>();
        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for(LineDTO lineDTO : lineDTOS) {
            String date = lineDTO.getDate();
            names.add(date);
            Integer value = lineDTO.getValue();
            values.add(value);
        }
        map.put("names", names);
        map.put("values", values);

        return ResultVOUtil.success(map);
    }


    //折线图堆叠
    @GetMapping("/stackedLineSale")
    public ResultVO stackedLineSale(){
        StackedLineVO vo = new StackedLineVO();
        List<String> names = this.orderDetailMapper.getProductNames();
        vo.setNames(names);
        List<String> dates = this.orderDetailMapper.getStackedDate();
        vo.setDates(dates);
        List<StackedLineInnerVO> list = new ArrayList<>();
        for (String name : names) {
            List<Integer> datas = this.orderDetailMapper.getStackedData(name);
            StackedLineInnerVO innerVO = new StackedLineInnerVO();
            innerVO.setName(name);
            innerVO.setData(datas);
            list.add(innerVO);
        }

        vo.setDatas(list);
        return ResultVOUtil.success(vo);
    }
}

