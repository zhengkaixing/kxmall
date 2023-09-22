package com.kxmall.user.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 用户视图对象 kx_user
 *
 * @author kxmall
 * @date 2023-02-14
 */
@Data
@ExcelIgnoreUnannotated
public class KxUserVo {

    private static final long serialVersionUID = 1L;

    private String accessToken;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private Long uid;

    /**
     * 用户账户(跟accout一样)
     */
    @ExcelProperty(value = "用户账户(跟accout一样)")
    private String username;


    /**
     * 真实姓名
     */
    @ExcelProperty(value = "真实姓名")
    private String realName;

    /**
     * 生日
     */
    @ExcelProperty(value = "生日")
    private Long birthday;

    /**
     * 身份证号码
     */
    @ExcelProperty(value = "身份证号码")
    private String cardId;

    /**
     * 用户备注
     */
    @ExcelProperty(value = "用户备注")
    private String mark;

    /**
     * 合伙人id
     */
    @ExcelProperty(value = "合伙人id")
    private Long partnerId;

    /**
     * 用户分组id
     */
    @ExcelProperty(value = "用户分组id")
    private Long groupId;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @ExcelProperty(value = "用户头像")
    private String avatar;

    /**
     * 手机号码
     */
    @ExcelProperty(value = "手机号码")
    private String phone;

    /**
     * 添加ip
     */
    @ExcelProperty(value = "添加ip")
    private String addIp;

    /**
     * 最后一次登录ip
     */
    @ExcelProperty(value = "最后一次登录ip")
    private String lastIp;

    /**
     * 用户余额
     */
    @ExcelProperty(value = "用户余额")
    private BigDecimal nowMoney;

    /**
     * 佣金金额
     */
    @ExcelProperty(value = "佣金金额")
    private BigDecimal brokeragePrice;

    /**
     * 用户剩余积分
     */
    @ExcelProperty(value = "用户剩余积分")
    private BigDecimal integral;

    /**
     * 连续签到天数
     */
    @ExcelProperty(value = "连续签到天数")
    private Long signNum;

    /**
     * 1为正常，0为禁止
     */
    @ExcelProperty(value = "1为正常，0为禁止")
    private Integer status;

    /**
     * 等级
     */
    @ExcelProperty(value = "等级")
    private Integer level;

    /**
     * 推广元id
     */
    @ExcelProperty(value = "推广元id")
    private Long spreadUid;

    /**
     * 推广员关联时间
     */
    @ExcelProperty(value = "推广员关联时间")
    private Date spreadTime;

    /**
     * 用户类型
     */
    @ExcelProperty(value = "用户类型")
    private String userType;

    /**
     * 是否为推广员
     */
    @ExcelProperty(value = "是否为推广员")
    private Integer isPromoter;

    /**
     * 用户购买次数
     */
    @ExcelProperty(value = "用户购买次数")
    private Long payCount;

    /**
     * 下级人数
     */
    @ExcelProperty(value = "下级人数")
    private Long spreadCount;

    /**
     * 详细地址
     */
    @ExcelProperty(value = "详细地址")
    private String addres;

    /**
     * 管理员编号
     */
    @ExcelProperty(value = "管理员编号 ")
    private Long adminid;

    /**
     * 用户登陆类型，h5,wechat,routine
     */
    @ExcelProperty(value = "用户登陆类型，h5,wechat,routine")
    private Integer loginType;

    /**
     * 微信用户json信息
     */
    @ExcelProperty(value = "微信用户json信息")
    private String wxProfile;

    /**
     * openId
     */
    private String openId;

    /**
     * sessionKey
     */
    private String sessionKey;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Integer isDel;


}
