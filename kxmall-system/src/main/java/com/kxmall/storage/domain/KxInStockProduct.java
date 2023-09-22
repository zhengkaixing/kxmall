package com.kxmall.storage.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 入库商品对象 kx_in_stock_product
 *
 * @author kxmall
 * @date 2023-08-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_in_stock_product")
public class KxInStockProduct extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 商品类目
     */
    private String categoryName;
    /**
     * 商品条码
     */
    private String barCode;
    /**
     * 入库单号
     */
    private String inStockNumbers;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品规格
     */
    private String productAttrName;
    /**
     * 库存可用量
     */
    private Long stock;
    /**
     * 入库数量
     */
    private Long inStockNum;
    /**
     * 商品规格id
     */
    private Long productAttrId;

}
