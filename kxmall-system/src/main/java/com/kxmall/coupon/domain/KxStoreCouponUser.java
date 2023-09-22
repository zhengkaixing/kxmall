package com.kxmall.coupon.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券发放记录对象 kx_store_coupon_user
 *
 * @author kxmall
 * @date 2023-02-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_coupon_user")
public class KxStoreCouponUser extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 优惠券发放记录id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 兑换的项目id
     */
    private Long cid;
    /**
     * 优惠券所属用户
     */
    private Long uid;
    /**
     * 优惠券名称
     */
    private String couponTitle;
    /**
     * 优惠券的面值
     */
    private BigDecimal couponPrice;
    /**
     * 最低消费多少金额可用优惠券
     */
    private BigDecimal useMinPrice;
    /**
     * 优惠券结束时间
     */
    private Date endTime;
    /**
     * 使用时间
     */
    private Date useTime;
    /**
     * 获取方式
     */
    private String type;
    /**
     * 状态（0：未使用，1：已使用, 2:已过期）
     */
    private Integer status;
    /**
     * 是否有效
     */
    private Integer isFail;
    /**
     *
     */
    private Integer isDel;

}
