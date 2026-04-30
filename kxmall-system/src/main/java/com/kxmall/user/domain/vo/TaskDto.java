package com.kxmall.user.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class TaskDto {

    private List<KxUserTaskVo> list;
    private Long reachCount;
    private List<KxUserTaskVo> task;
}

