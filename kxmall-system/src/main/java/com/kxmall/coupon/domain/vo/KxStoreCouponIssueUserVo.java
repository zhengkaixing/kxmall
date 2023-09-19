package com.kxmall.coupon.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 优惠券前台用户领取记录视图对象 kx_store_coupon_issue_user
 *
 * @author kxmall
 * @date 2023-02-17
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreCouponIssueUserVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 领取优惠券用户ID
     */
    @ExcelProperty(value = "领取优惠券用户ID")
    private Long uid;

    /**
     * 优惠券前台领取ID
     */
    @ExcelProperty(value = "优惠券前台领取ID")
    private Integer issueCouponId;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Integer isDel;


}
