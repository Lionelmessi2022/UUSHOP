package com.test.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseEnum {
    PRODUCT_NULL(300,"商品信息不存在"),
    PRODUCT_STOCK_EMPTY(301,"商品库存不足"),
    PRODUCT_SUBSTOCK_ERROR(302,"商品减库存失败"),
    ORDER_NULL(303,"订单信息不存在"),
    ORDER_FINISH(304,"已完成的订单不能取消"),
    ORDER_CANCEL(305,"已取消的订单不能取消"),
    ORDER_FINISH_ERROR(306,"已完成的订单完成不能重复完成"),
    ORDER_CANCEL_ERROR(307,"已取消的订单不能完成"),
    ORDER_FINISH_FAIL(308,"订单完成失败"),
    ORDER_FINISH_PAT_ERROR(309,"已完结的订单不能支付"),
    ORDER_CANCEL_PAY_ERROR(310,"已取消的订单不能支付"),
    ORDER_PAY_ERROR(311,"已支付的订单不能重复支付"),
    ORDER_PAY_FAIL(312,"订单支付失败"),
    MOBILE_ERROR(313,"手机号格式错误"),
    MOBILE_EXIST(314,"手机号已被注册"),
    USER_REGISTER_ERROR(315,"用户注册失败"),

    MOBILE_NULL(316,"手机号为空"),
    PASSWORD_NULL(317,"密码为空"),
    CODE_NULL(318,"验证码为空"),
    MOBILE_NOT_EXIST(319,"手机号未注册"),
    PASSWORD_ERROR(320,"密码错误"),
    USER_TOKEN_ERROR(321,"用户token失效"),
    SMS_SEND_ERROR(322,"短信发送失败"),
    PRODUCT_ADD_ERROR(323,"商品添加失败"),
    PRODUCT_CATEGORY_NOT_EXIST(324,"商品类别不存在"),
    PRODUCT_DELETE_ERROR(325,"商品删除失败"),
    PRODUCT_STATUS_ERROR(326,"商品状态更新失败"),
    PRODUCT_STATUS_TRUE_CHANGE_ERROR(327,"上架商品请勿重复操作"),
    PRODUCT_STATUS_FALSE_CHANGE_ERROR(328,"下架商品请勿重复操作"),
    PRODUCT_UPDATE_ERROR(329,"商品修改失败"),
    ADMIN_USERNAME_NULL(330,"管理员用户名不存在"),
    ADMIN_PASSWORD_ERROR(331,"管理员密码错误"),
    ADMIN_TOKEN_ERROR(332,"管理员登录失效"),
    ORDER_NOT_PAY(333,"订单未支付,无法完结"),
    //验证码错误
    CODE_ERROR(334,"验证码错误");




    private Integer code;
    private String msg;
}
