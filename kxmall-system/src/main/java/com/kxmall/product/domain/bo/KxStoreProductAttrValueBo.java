package com.kxmall.product.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品属性值业务对象 kx_store_product_attr_value
 *
 * @author kxmall
 * @date 2023-02-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxStoreProductAttrValueBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品属性索引值 (attr_value|attr_value[|....])
     */
    private String sku;

    /**
     * 属性对应的库存
     */
    private Integer stock;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 属性金额
     */
    private BigDecimal price;

    /**
     * 图片
     */
    private String image;

    /**
     * 唯一值
     */
    private String unique;

    /**
     * 成本价
     */
    private BigDecimal cost;

    /**
     * 商品条码
     */
    private String barCode;

    /**
     * 原价
     */
    private BigDecimal otPrice;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 体积
     */
    private BigDecimal volume;

    /**
     * 一级返佣
     */
    private BigDecimal brokerage;

    /**
     * 二级返佣
     */
    private BigDecimal brokerageTwo;

    /**
     * 拼团价
     */
    private BigDecimal pinkPrice;

    /**
     * 拼团库存
     */
    private Integer pinkStock;

    /**
     * 秒杀价
     */
    private BigDecimal seckillPrice;

    /**
     * 秒杀库存
     */
    private Integer seckillStock;

    /**
     * 需要多少积分兑换
     */
    private Integer integral;


}
