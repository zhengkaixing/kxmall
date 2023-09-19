package com.kxmall.storage.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kxmall.common.annotation.ExcelDictFormat;
import com.kxmall.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 入库商品视图对象 kx_in_stock_product
 *
 * @author kxmall
 * @date 2023-08-29
 */
@Data
@ExcelIgnoreUnannotated
public class KxInStockProductVo {

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
     * 入库单号
     */
    @ExcelProperty(value = "入库单号")
    private String inStockNumbers;

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
     * 入库数量
     */
    @ExcelProperty(value = "入库数量")
    private Long inStockNum;

    /**
     * 商品规格id
     */
    @ExcelProperty(value = "商品规格id")
    private Long productAttrId;


}
