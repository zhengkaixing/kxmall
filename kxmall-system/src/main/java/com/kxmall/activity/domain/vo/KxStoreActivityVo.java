package com.kxmall.activity.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 活动商品视图对象 kx_store_activity
 *
 * @author kxmall
 * @date 2024-08-07
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreActivityVo {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @ExcelProperty(value = "活动id")
    private Long id;

    /**
     * 活动标题
     */
    @ExcelProperty(value = "活动标题")
    private String title;

    /**
     * 活动海报
     */
    @ExcelProperty(value = "活动海报")
    private String imgUrl;

    /**
     * 海报链接
     */
    @ExcelProperty(value = "海报链接")
    private String outUrl;

    /**
     * 活动详情
     */
    @ExcelProperty(value = "活动详情")
    private String description;

    /**
     * 状态(是否开启)
     */
    @ExcelProperty(value = "状态(是否开启)")
    private Integer status;





}
