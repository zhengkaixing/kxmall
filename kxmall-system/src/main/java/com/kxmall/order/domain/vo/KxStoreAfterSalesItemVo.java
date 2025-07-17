package com.kxmall.order.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 售后子视图对象 kx_store_after_sales_item
 *
 * @author kxmall
 * @date 2024-11-23
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreAfterSalesItemVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ExcelProperty(value = "主键id")
    private Long id;

    /**
     * 售后id
     */
    @ExcelProperty(value = "售后id")
    private Long storeAfterSalesId;

    /**
     * 商品id
     */
    @ExcelProperty(value = "商品id")
    private Long productId;

    /**
     * 退货东西的详情信息
     */
    @ExcelProperty(value = "退货东西的详情信息")
    private String cartInfo;

    /**
     * 删除状态
     */
    @ExcelProperty(value = "删除状态")
    private Integer isDel;


}
