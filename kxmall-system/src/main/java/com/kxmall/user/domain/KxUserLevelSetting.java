package com.kxmall.user.domain;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 设置用户等级对象 kx_user_level_setting
 *
 * @author kxmall
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "kx_user_level_setting", autoResultMap = true)
public class KxUserLevelSetting extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 商户id
     */
    private Long merId;
    /**
     * 会员名称
     */
    private String name;
    /**
     * 购买金额
     */
    private BigDecimal money;
    /**
     * 有效时间
     */
    private Long validDate;
    /**
     * 是否为永久会员
     */
    private Integer isForever;
    /**
     * 是否购买,1=购买,0=不购买
     */
    private Integer isPay;
    /**
     * 是否显示 1=显示,0=隐藏
     */
    private Integer isShow;
    /**
     * 会员等级
     */
    private Long grade;
    /**
     * 享受折扣
     */
    private BigDecimal discount;
    /**
     * 会员卡背景
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONArray image;
    /**
     * 会员图标
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONArray icon;
    /**
     * 说明
     */
    @TableField(value = "`explain`")
    private String explain;
    /**
     * 是否删除.1=删除,0=未删除
     */
    private Integer isDel;

}
