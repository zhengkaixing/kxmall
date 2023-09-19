package com.kxmall.product.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品属性详情对象 kx_store_product_attr_result
 *
 * @author kxmall
 * @date 2023-02-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_product_attr_result")
public class KxStoreProductAttrResult extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 商品属性参数
     */
    private String result;
    /**
     * 上次修改时间
     */
    private Date changeTime;

}
