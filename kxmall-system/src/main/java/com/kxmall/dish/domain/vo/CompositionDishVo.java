package com.kxmall.dish.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Generate Code By kxmall
 */
@Data
@ExcelIgnoreUnannotated
public class CompositionDishVo  {

    private static final long serialVersionUID=1L;

    private Long storageId;

    private String title;

    private Integer sales;

    private BigDecimal score;

    private String img;
    private String detail;
    private String description;
    private Long categoryId;
    private Integer status;

}
