package com.kxmall.user.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 签到记录视图对象 kx_user_sign
 *
 * @author kxmall
 * @date 2024-08-26
 */
@Data
@ExcelIgnoreUnannotated
public class KxUserSignVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 用户uid
     */
    @ExcelProperty(value = "用户uid")
    private Long uid;

    /**
     * 签到说明
     */
    @ExcelProperty(value = "签到说明")
    private String title;

    /**
     * 获得积分
     */
    @ExcelProperty(value = "获得积分")
    private Long number;

    /**
     * 剩余积分
     */
    @ExcelProperty(value = "剩余积分")
    private Long balance;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Integer isDel;


    private String nickname;

    private Date createTime;
}
