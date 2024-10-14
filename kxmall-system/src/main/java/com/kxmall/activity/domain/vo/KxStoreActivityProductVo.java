package com.kxmall.activity.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 活动商品视图对象 kx_store_activity_product
 *
 * @author kxmall
 * @date 2024-08-07
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreActivityProductVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 商品id
     */
    @ExcelProperty(value = "商品id")
    private Long productId;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    private String productName;

    /**
     * 活动id
     */
    @ExcelProperty(value = "活动id")
    private Long activityId;

    /**
     * 活动名称
     */
    @ExcelProperty(value = "活动名称")
    private String activityTitle;


    /**
     * 活动价
     */
    @ExcelProperty(value = "活动价")
    private BigDecimal activityPrice;


    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * vip价格
     */
    private BigDecimal vipPrice;

    /**
     * 销量
     */
    private Long sales;

    /**
     * 图片
     */
    private String img;


    /**
     * 标题
     */
    private String title;

    /**
     * 分类
     */
    private Long categoryId;


    /**
     * 规则id
     */
    private Long productAttrId;

    /**
     * 单位
     */
    private String unitName;


    /**
     * 商品简介
     */
    private String storeInfo;



}
