package com.kxmall.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户会员等级记录（对应商用版 kx_user_level 记录表语义）。
 * 开源版 {@link KxUserLevel} 已用于「等级配置」表，故记录表单独映射为 kx_user_member_level。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_user_member_level")
public class KxUserMemberLevel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long uid;

    private Long levelId;

    private Long grade;

    private Long validTime;

    private Integer isForever;

    private Long merId;

    private Integer status;

    private String mark;

    private Integer remind;

    private Integer isDel;

    private Integer discount;
}
