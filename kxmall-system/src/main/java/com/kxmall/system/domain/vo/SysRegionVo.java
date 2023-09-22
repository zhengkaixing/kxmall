package com.kxmall.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;


/**
 * 中国地区系统视图对象 sys_region
 *
 * @author kxmall
 * @date 2023-02-08
 */
@Data
@ExcelIgnoreUnannotated
public class SysRegionVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 城市id
     */
    @ExcelProperty(value = "城市id")
    private Long cityId;

    /**
     * 省市级别
     */
    @ExcelProperty(value = "省市级别")
    private Long level;

    /**
     * 父级id
     */
    @ExcelProperty(value = "父级id")
    private Long parentId;

    /**
     * 区号
     */
    @ExcelProperty(value = "区号")
    private String areaCode;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 合并名称
     */
    @ExcelProperty(value = "合并名称")
    private String mergerName;

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
     * 是否展示
     */
    @ExcelProperty(value = "是否展示")
    private Integer isShow;


    @TableField(exist = false)
    private List<SysRegionVo> children;
}
