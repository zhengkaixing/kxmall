package com.kxmall.dish.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * Generate Code By kxmall
 */

@Data
@ExcelIgnoreUnannotated
public class CompositionDishItemVo {

    private static final long serialVersionUID=1L;

    private Long skuId;
    private String barCode;
    private Long dishId;

    //商品类目
    private String category;

    @TableField(exist = false)
    private Integer num;

    private String unit;

    //商品名称
    private String spuName;


    //商品名称
    private String skuName;
}
