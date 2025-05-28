package com.kxmall.dish.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * Generate Code By kxmall
 */

@Data
@TableName("kx_composition_dish_item")
public class CompositionDishItemDO extends BaseEntity {

    @TableId(value = "id")
    private Long id;

    private Long productId;
    private String barCode;
    private Long dishId;

    //商品类目
    private String categoryName;

    private Long num;

    private String unit;

    //商品名称
    private String productName;


    //商品名称
    private String productAttrName;
}
