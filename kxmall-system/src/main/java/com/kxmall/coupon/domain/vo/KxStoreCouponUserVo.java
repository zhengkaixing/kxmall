package com.kxmall.coupon.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kxmall.common.annotation.ExcelDictFormat;
import com.kxmall.common.convert.ExcelDictConvert;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 优惠券发放记录视图对象 kx_store_coupon_user
 *
 * @author kxmall
 * @date 2023-02-17
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreCouponUserVo {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券发放记录id
     */
    @ExcelProperty(value = "优惠券发放记录id")
    private Long id;

    /**
     * 兑换的项目id
     */
    @ExcelProperty(value = "兑换的项目id")
    private Long cid;

    /**
     * 优惠券所属用户
     */
    @ExcelProperty(value = "优惠券所属用户")
    private Long uid;

    /**
     * 优惠券名称
     */
    @ExcelProperty(value = "优惠券名称")
    private String couponTitle;

    /**
     * 优惠券的面值
     */
    @ExcelProperty(value = "优惠券的面值")
    private BigDecimal couponPrice;

    /**
     * 最低消费多少金额可用优惠券
     */
    @ExcelProperty(value = "最低消费多少金额可用优惠券")
    private BigDecimal useMinPrice;

    /**
     * 优惠券结束时间
     */
    @ExcelProperty(value = "优惠券结束时间")
    private Date endTime;

    /**
     * 使用时间
     */
    @ExcelProperty(value = "使用时间")
    private Date useTime;

    /**
     * 获取方式
     */
    @ExcelProperty(value = "获取方式")
    private String type;

    /**
     * 状态（0：未使用，1：已使用, 2:已过期）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：未使用，1：已使用,,2=:已过期")
    private Integer status;

    /**
     * 是否有效
     */
    @ExcelProperty(value = "是否有效")
    private Integer isFail;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Integer isDel;


}
