package com.kxmall.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_user_task")
public class KxUserTask extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
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
    private Integer isDel;
}