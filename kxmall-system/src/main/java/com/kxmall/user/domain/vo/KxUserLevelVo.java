package com.kxmall.user.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 用户等级视图对象 kx_user_level
 *
 * @author kxmall
 * @date 2023-02-14
 */
@Data
@ExcelIgnoreUnannotated
public class KxUserLevelVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 商户id
     */
    @ExcelProperty(value = "商户id")
    private Long merId;

    /**
     * 会员名称
     */
    @ExcelProperty(value = "会员名称")
    private String name;

    /**
     * 购买金额
     */
    @ExcelProperty(value = "购买金额")
    private BigDecimal money;

    /**
     * 有效时间
     */
    @ExcelProperty(value = "有效时间")
    private Long validDate;

    /**
     * 是否为永久会员
     */
    @ExcelProperty(value = "是否为永久会员")
    private Integer isForever;

    /**
     * 是否购买,1=购买,0=不购买
     */
    @ExcelProperty(value = "是否购买,1=购买,0=不购买")
    private Integer isPay;

    /**
     * 是否显示 1=显示,0=隐藏
     */
    @ExcelProperty(value = "是否显示 1=显示,0=隐藏")
    private Integer isShow;

    /**
     * 会员等级
     */
    @ExcelProperty(value = "会员等级")
    private Long grade;

    /**
     * 享受折扣
     */
    @ExcelProperty(value = "享受折扣")
    private BigDecimal discount;

    /**
     * 会员卡背景
     */
    @ExcelProperty(value = "会员卡背景")
    private String image;

    /**
     * 会员图标
     */
    @ExcelProperty(value = "会员图标")
    private String icon;

    /**
     * 说明
     */
    @ExcelProperty(value = "说明")
    private String explain;

    /**
     * 是否删除.1=删除,0=未删除
     */
    @ExcelProperty(value = "是否删除.1=删除,0=未删除")
    private Integer isDel;


}
