package com.kxmall.order.domain.bo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description: 订单消息
 * @author: kxmall
 * @date: 2020/03/01 16:33
 **/
@Data
@ToString
public class OrderMessageBO implements Serializable {

    // 配送员主键
    private Long riderId;

    /*** ============用户信息==============*/

    private Long userId;

    // 收货人
    private String consignee;

    // 电话
    private String phone;

    // 省
    private String province;

    // 市
    private String city;

    // 区（县）
    private String county;

    // 地址
    private String address;

    // 备注
    private String mono;

    /*** ============仓库信息==================*/
    //仓库主键ID
    private Long storageId;

    // 仓库名称
    private String storeName;

    /*** ==============订单信息===============*/

    // 订单编号
    private String orderNo;

    //商品总价
    private BigDecimal totalPrice;

    //优惠总价
    private BigDecimal totalPreferentialPrice;

    // 配送费
    private BigDecimal freightPrice;

    // 支付方式
    private String payChannel;

    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 下单时间
     */
    private Date orderCreateTime;

    // 预计送达时间
    private Date predictTime;

    /*** 经度*/
    private BigDecimal longitude;

    /*** 纬度*/
    private BigDecimal latitude;


    // 商品信息
    private List<RiderProductBO> riderSpuBOList;
}
