package com.kxmall.group.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 团购商品视图对象 kx_group_shop_product
 *
 * @author kxmall
 * @date 2023-10-07
 */
@Data
@ExcelIgnoreUnannotated
public class KxGroupShopProductVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 团购商品
     */
    @ExcelProperty(value = "团购商品")
    private Long productAttrId;

    /**
     * 团购主键
     */
    @ExcelProperty(value = "团购主键")
    private Long groupShopId;

    /**
     * 团购价格
     */
    @ExcelProperty(value = "团购价格")
    private BigDecimal productGroupShopPrice;


}
