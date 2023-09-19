package com.kxmall.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 快递公司视图对象 sys_express
 *
 * @author kxmall
 * @date 2023-02-17
 */
@Data
@ExcelIgnoreUnannotated
public class SysExpressVo {

    private static final long serialVersionUID = 1L;

    /**
     * 快递公司id
     */
    @ExcelProperty(value = "快递公司id")
    private Long id;

    /**
     * 快递公司简称
     */
    @ExcelProperty(value = "快递公司简称")
    private String code;

    /**
     * 快递公司全称
     */
    @ExcelProperty(value = "快递公司全称")
    private String name;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sort;

    /**
     * 是否显示
     */
    @ExcelProperty(value = "是否显示")
    private Integer isShow;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long isDel;


}
