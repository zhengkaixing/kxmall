package com.kxmall.product.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 商品属性值视图对象 kx_store_product_attr_value
 *
 * @author kxmall
 * @date 2023-02-13
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreProductAttrValueVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 商品ID
     */
    @ExcelProperty(value = "商品ID")
    private Long productId;

    /**
     * 商品属性索引值 (attr_value|attr_value[|....])
     */
    @ExcelProperty(value = "商品属性索引值 (attr_value|attr_value[|....])")
    private String sku;

    /**
     * 属性对应的库存
     */
    @ExcelProperty(value = "属性对应的库存")
    private Integer stock;

    /**
     * 销量
     */
    @ExcelProperty(value = "销量")
    private Integer sales;

    /**
     * 属性金额
     */
    @ExcelProperty(value = "属性金额")
    private BigDecimal price;

    /**
     * 图片
     */
    @ExcelProperty(value = "图片")
    private String image;

    /**
     * 唯一值
     */
    @ExcelProperty(value = "唯一值")
    private String unique;

    /**
     * 成本价
     */
    @ExcelProperty(value = "成本价")
    private BigDecimal cost;

    /**
     * 商品条码
     */
    @ExcelProperty(value = "商品条码")
    private String barCode;

    /**
     * 原价
     */
    @ExcelProperty(value = "原价")
    private BigDecimal otPrice;

    /**
     * 重量
     */
    @ExcelProperty(value = "重量")
    private BigDecimal weight;

    /**
     * 体积
     */
    @ExcelProperty(value = "体积")
    private BigDecimal volume;

    /**
     * 一级返佣
     */
    @ExcelProperty(value = "一级返佣")
    private BigDecimal brokerage;

    /**
     * 二级返佣
     */
    @ExcelProperty(value = "二级返佣")
    private BigDecimal brokerageTwo;

    /**
     * 拼团价
     */
    @ExcelProperty(value = "拼团价")
    private BigDecimal pinkPrice;

    /**
     * 拼团库存
     */
    @ExcelProperty(value = "拼团库存")
    private Integer pinkStock;

    /**
     * 秒杀价
     */
    @ExcelProperty(value = "秒杀价")
    private BigDecimal seckillPrice;

    /**
     * 秒杀库存
     */
    @ExcelProperty(value = "秒杀库存")
    private Integer seckillStock;

    /**
     * 需要多少积分兑换
     */
    @ExcelProperty(value = "需要多少积分兑换")
    private Integer integral;


}
