package com.kxmall.user.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class KxUserTaskVo {

    private Long id;
    private String name;
    private String realName;
    private String taskType;
    private Integer number;
    private Integer levelId;
    private Integer sort;
    private Integer isShow;
    private Integer isMust;
    private String illustrate;
    private Date createTime;
    private Date updateTime;
    private Integer isDel;
    private String createBy;
    private String updateBy;

    // 额外字段
    private String levalName;
    private Integer newNumber;
    private Integer speed;
    private Integer finish;
    private String taskTypeTitle;
}