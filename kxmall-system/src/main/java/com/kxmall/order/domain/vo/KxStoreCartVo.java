package com.kxmall.order.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * 购物车视图对象 kx_store_cart
 *
 * @author kxmall
 * @date 2023-02-15
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreCartVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;




    /**
     * 购物车id
     */
    @ExcelProperty(value = "购物车id")
    private Long cartId;

    /**
     * 商品ID
     */
    @ExcelProperty(value = "商品ID")
    private Long productId;

    /**
     * 购买东西的详细信息
     */
    @ExcelProperty(value = "购买东西的详细信息")
    private String cartInfo;

    private Long productAttrId;
    private Integer cartNum;
    private String productName;
    private BigDecimal otPrice;
    private BigDecimal price;
    private String productAttrName;
    private String productImg;
    private String productAttrImg;
    private Integer stock;
    private Long cateId;
    private List<Long> cateIdList;
}
