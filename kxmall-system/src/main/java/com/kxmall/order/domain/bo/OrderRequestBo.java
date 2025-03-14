package com.kxmall.order.domain.bo;

import com.kxmall.coupon.domain.KxStoreCouponUser;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2019/7/6.
 */
@Data
public class OrderRequestBo {

    private List<OrderRequestProductBo> productList;

    /**
     * 商品支付总价
     */
    private BigDecimal totalPrice;

    /**
     * 0骑手配置 1 到店自提
     */
    private Integer shippingType;

    private BigDecimal totalOriginalPrice;

    private Long addressId;

    private Long groupShopId;

    /**
     * 秒杀
     */
    private Long seckillId;

    private String mono;

    /**
     * 购物车 ？ 直接点击购买商品
     */
    private String takeWay;

    /**
     * 渠道
     */
    private String channel;

    private BigDecimal freightPrice;

    /**
     * 预计到达时间
     */
    private String predictTime;

    private Date predictDate;

    private Long storageId;

    /**
     * 使用积分内容
     */
    private Long integral;

    /**
     * 是否是积分订单
     */
    private Long isIntegral;




    /**
     * 到店取货联系人
     */
    private String name;
    /**
     * 到店取货电话
     */
    private String phone;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 优惠券对象
     */
    private KxStoreCouponUser coupon;

}
