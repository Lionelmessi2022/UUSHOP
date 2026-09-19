package com.test.mapper;

import com.test.dto.BarDTO;
import com.test.dto.LineDTO;
import com.test.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.bouncycastle.LICENSE;

import java.util.List;

/**
 * <p>
 * 订单详情表 Mapper 接口
 * </p>
 *
 * @author test
 * @since 2026-08-16
 */
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    @Select({"select product_name name,sum(product_quantity) value from order_detail group by product_name"})
    public List<BarDTO> barList();

    @Select({"select date_format(create_time,'%Y-%m-%d') date,sum(product_quantity) value from order_detail group by date_format(create_time,'%Y-%m-%d')"})
    public List<LineDTO> lineList();

    @Select({"select product_name from product_info"})
    public List<String> getProductNames();

    @Select({"select distinct date_format(create_time,'%Y-%m-%d') from order_detail"})
    public List<String> getStackedDate();

    @Select({"select (\n" +
            "    select COALESCE(sum(product_quantity),0)\n" +
            "    from order_detail where\n" +
            "    pi.product_id = order_detail.product_id and\n" +
            "    DATE_FORMAT(order_detail.create_time, '%Y-%m-%d') = mm.dd\n" +
            "    ) as count\n" +
            "from product_info pi,\n" +
            "     (select distinct DATE_FORMAT(order_detail.create_time, '%Y-%m-%d') as dd from order_detail)\n" +
            "         as mm\n" +
            "where pi.product_name = #{productName}  order by mm.dd"})
    public List<Integer> getStackedData(String productName);




}
