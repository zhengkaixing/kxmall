package com.kxmall.decorate.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 首页页面模板视图对象 kx_page_template
 *
 * @author kxmall
 * @date 2023-11-05
 */
@Data
@ExcelIgnoreUnannotated
public class KxPageTemplateVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 0停用1启用
     */
    @ExcelProperty(value = "0停用1启用")
    private Integer status;

    /**
     * 首页json格式
     */
    @ExcelProperty(value = "首页json格式")
    private String content;


}
