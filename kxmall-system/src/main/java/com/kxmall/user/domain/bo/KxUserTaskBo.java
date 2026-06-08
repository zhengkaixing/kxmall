package com.kxmall.user.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class KxUserTaskBo extends BaseEntity {

    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    @NotBlank(message = "任务名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    private String realName;

    @NotBlank(message = "任务类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taskType;

    @NotNull(message = "限定数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer number;

    @NotNull(message = "等级ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer levelId;

    private Integer sort;
    private Integer isShow;
    private Integer isMust;
    private String illustrate;
    private Integer isDel;
}
