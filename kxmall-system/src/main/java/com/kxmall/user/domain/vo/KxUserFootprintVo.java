package com.kxmall.user.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 足迹视图对象 kx_user_footprint
 *
 * @author kxmall
 * @date 2023-04-06
 */
@Data
@ExcelIgnoreUnannotated
public class KxUserFootprintVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
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


}
