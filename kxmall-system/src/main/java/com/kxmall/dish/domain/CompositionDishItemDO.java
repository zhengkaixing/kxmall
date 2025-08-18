package com.kxmall.dish.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 组合菜品项实体类
 * Generate Code By kxmall
 */
@Data
@TableName("kx_composition_dish_item")
public class CompositionDishItemDO extends BaseEntity {

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品条形码
     */
    private String barCode;

    /**
     * 所属菜品ID
     */
    private Long dishId;

    /**
     * 商品类目名称
     */
    private String categoryName;

    /**
     * 商品数量
     */
    private Long num;

    /**
     * 单位
     */
    private String unit;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品属性名称
     */
    private String productAttrName;
}