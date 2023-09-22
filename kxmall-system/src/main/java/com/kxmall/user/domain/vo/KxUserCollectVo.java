package com.kxmall.user.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kxmall.common.annotation.ExcelDictFormat;
import com.kxmall.common.convert.ExcelDictConvert;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 客户收藏视图对象 kx_user_collect
 *
 * @author kxmall
 * @date 2023-04-06
 */
@Data
@ExcelIgnoreUnannotated
public class KxUserCollectVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private Long userId;

    /**
     * 商品id
     */
    @ExcelProperty(value = "商品id")
    private Long productId;



    private BigDecimal otPrice;
    private BigDecimal price;
    private BigDecimal vipPirce;
    private String productName;
    private Integer sales;
    private String img;
    private String description;
    private String unitName;
    private Integer status;

}
