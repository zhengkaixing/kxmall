package com.kxmall.storage.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kxmall.common.annotation.ExcelDictFormat;
import com.kxmall.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 出库商品视图对象 kx_out_stock_product
 *
 * @author kxmall
 * @date 2023-08-29
 */
@Data
@ExcelIgnoreUnannotated
public class KxOutStockProductVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 商品类目
     */
    @ExcelProperty(value = "商品类目")
    private String categoryName;

    /**
     * 商品条码
     */
    @ExcelProperty(value = "商品条码")
    private String barCode;

    /**
     * 出库单号
     */
    @ExcelProperty(value = "出库单号")
    private String outStockNumbers;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    private String productName;

    /**
     * 商品规格
     */
    @ExcelProperty(value = "商品规格")
    private String productAttrName;

    /**
     * 库存可用量
     */
    @ExcelProperty(value = "库存可用量")
    private Long stock;

    /**
     * 出库数量
     */
    @ExcelProperty(value = "出库数量")
    private Long outStockNum;

    /**
     * 商品规格id
     */
    @ExcelProperty(value = "商品规格id")
    private Long productAttrId;


}
