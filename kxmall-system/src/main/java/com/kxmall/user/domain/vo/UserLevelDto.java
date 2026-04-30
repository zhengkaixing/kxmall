package com.kxmall.user.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserLevelDto {
    private List<KxUserLevelSettingVo> list;
    private TaskDto task;
    private Long grade;
}

