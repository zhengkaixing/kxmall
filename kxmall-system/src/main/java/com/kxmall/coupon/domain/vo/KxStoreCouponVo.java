package com.kxmall.coupon.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kxmall.product.domain.KxStoreProduct;
import com.kxmall.common.annotation.ExcelDictFormat;
import com.kxmall.common.convert.ExcelDictConvert;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * 优惠券视图对象 kx_store_coupon
 *
 * @author kxmall
 * @date 2023-02-17
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreCouponVo {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券表ID
     */
    @ExcelProperty(value = "优惠券表ID")
    private Long id;

    /**
     * 优惠券名称
     */
    @ExcelProperty(value = "优惠券名称")
    private String title;

    /**
     * 兑换消耗积分值
     */
    @ExcelProperty(value = "兑换消耗积分值")
    private Long integral;

    /**
     * 兑换的优惠券面值
     */
    @ExcelProperty(value = "兑换的优惠券面值")
    private BigDecimal couponPrice;

    /**
     * 最低消费多少金额可用优惠券
     */
    @ExcelProperty(value = "最低消费多少金额可用优惠券")
    private BigDecimal useMinPrice;

    /**
     * 优惠券有效期限（单位：天）
     */
    @ExcelProperty(value = "优惠券有效期限", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "单=位：天")
    private Long couponTime;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sort;

    /**
     * 状态（0：关闭，1：开启）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：关闭，1：开启")
    private Integer status;

    /**
     * 商品ids
     */
    @ExcelProperty(value = "商品ids")
    private String productId;

    /**
     * 优惠券类型 0-通用 1-商品券
     */
    @ExcelProperty(value = "优惠券类型 0-通用 1-商品券")
    private Long type;

    /**
     * 是否删除
     */
    @ExcelProperty(value = "是否删除")
    private Integer isDel;

    private List<KxStoreProduct> product;

}
