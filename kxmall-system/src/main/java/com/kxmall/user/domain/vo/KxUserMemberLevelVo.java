package com.kxmall.user.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户会员等级记录 VO（App 端查询用）
 */
@Data
public class KxUserMemberLevelVo implements Serializable {

    private static final long serialVersionUID = 1L;

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
