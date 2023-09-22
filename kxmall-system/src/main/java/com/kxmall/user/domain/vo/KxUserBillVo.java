package com.kxmall.user.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 用户账单视图对象 kx_user_bill
 *
 * @author kxmall
 * @date 2023-02-14
 */
@Data
@ExcelIgnoreUnannotated
public class KxUserBillVo {

    private static final long serialVersionUID = 1L;

    /**
     * 用户账单id
     */
    @ExcelProperty(value = "用户账单id")
    private Long id;

    /**
     * 用户uid
     */
    @ExcelProperty(value = "用户uid")
    private Long uid;

    /**
     * 关联id
     */
    @ExcelProperty(value = "关联id")
    private String linkId;

    /**
     * 0 = 支出 1 = 获得
     */
    @ExcelProperty(value = "0 = 支出 1 = 获得")
    private Integer pm;

    /**
     * 账单标题
     */
    @ExcelProperty(value = "账单标题")
    private String title;

    /**
     * 明细种类
     */
    @ExcelProperty(value = "明细种类")
    private String category;

    /**
     * 明细类型
     */
    @ExcelProperty(value = "明细类型")
    private String type;

    /**
     * 明细数字
     */
    @ExcelProperty(value = "明细数字")
    private BigDecimal number;

    /**
     * 剩余
     */
    @ExcelProperty(value = "剩余")
    private BigDecimal balance;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String mark;


    private String nickname;

    /**
     * 0 = 带确定 1 = 有效 -1 = 无效
     */
    @ExcelProperty(value = "0 = 带确定 1 = 有效 -1 = 无效")
    private Integer status;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Integer isDel;

    private Date createTime;
}
