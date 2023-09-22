package com.kxmall.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


/**
 * 商品规格视图对象 kx_store_product_rule
 *
 * @author kxmall
 * @date 2023-02-08
 */
@Data
@ExcelIgnoreUnannotated
@TableName(autoResultMap = true)
public class KxStoreProductRuleVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 规格名称
     */
    @ExcelProperty(value = "规格名称")
    private String ruleName;

    /**
     * 规格值
     */
    @ExcelProperty(value = "规格值")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONArray ruleValue;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Integer isDel;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
