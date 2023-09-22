package com.kxmall.region.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 中国地区信息视图对象 kx_region
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Data
@ExcelIgnoreUnannotated
public class KxRegionVo {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @ExcelProperty(value = "自增ID")
    private Long id;

    /**
     * 代码
     */
    @ExcelProperty(value = "代码")
    private String code;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 简称
     */
    @ExcelProperty(value = "简称")
    private String shortName;

    /**
     * 上级代码
     */
    @ExcelProperty(value = "上级代码")
    private String superiorCode;

    /**
     * 经度
     */
    @ExcelProperty(value = "经度")
    private String lng;

    /**
     * 纬度
     */
    @ExcelProperty(value = "纬度")
    private String lat;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer sort;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String ramark;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Long state;

    /**
     * 租户ID
     */
    @ExcelProperty(value = "租户ID")
    private String tenantCode;

    /**
     * 级别
     */
    @ExcelProperty(value = "级别")
    private Long level;


}
