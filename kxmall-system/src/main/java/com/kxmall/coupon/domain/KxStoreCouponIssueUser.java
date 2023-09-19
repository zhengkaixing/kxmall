package com.kxmall.coupon.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 优惠券前台用户领取记录对象 kx_store_coupon_issue_user
 *
 * @author kxmall
 * @date 2023-02-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_coupon_issue_user")
public class KxStoreCouponIssueUser extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 领取优惠券用户ID
     */
    private Long uid;
    /**
     * 优惠券前台领取ID
     */
    private Integer issueCouponId;
    /**
     *
     */
    private Integer isDel;

}
