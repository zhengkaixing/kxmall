package com.kxmall.coupon.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 优惠券对象 kx_store_coupon
 *
 * @author kxmall
 * @date 2023-02-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_coupon")
public class KxStoreCoupon extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 优惠券表ID
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 优惠券名称
     */
    private String title;
    /**
     * 兑换消耗积分值
     */
    private Long integral;
    /**
     * 兑换的优惠券面值
     */
    private BigDecimal couponPrice;
    /**
     * 最低消费多少金额可用优惠券
     */
    private BigDecimal useMinPrice;
    /**
     * 优惠券有效期限（单位：天）
     */
    private Long couponTime;
    /**
     * 排序
     */
    private Long sort;
    /**
     * 状态（0：关闭，1：开启）
     */
    private Integer status;
    /**
     * 商品ids
     */
    private String productId;
    /**
     * 优惠券类型 0-通用 1-商品券
     */
    private Long type;
    /**
     * 是否删除
     */
    private Integer isDel;

}
