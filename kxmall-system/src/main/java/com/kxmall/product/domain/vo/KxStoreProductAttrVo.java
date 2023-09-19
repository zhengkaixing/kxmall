package com.kxmall.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 商品属性视图对象 kx_store_product_attr
 *
 * @author kxmall
 * @date 2023-02-13
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreProductAttrVo {

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
     * 属性名
     */
    @ExcelProperty(value = "属性名")
    private String attrName;

    /**
     * 属性值
     */
    @ExcelProperty(value = "属性值")
    private String attrValues;

    /**
     * 删除状态
     */
    @ExcelProperty(value = "删除状态")
    private Integer isDel;


}
