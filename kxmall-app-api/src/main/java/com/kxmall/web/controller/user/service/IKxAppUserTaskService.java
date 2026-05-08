package com.kxmall.web.controller.user.service;

import com.kxmall.user.domain.vo.TaskDto;

public interface IKxAppUserTaskService {

    TaskDto getTaskList(Long id, Long userId);

    Long getTaskComplete(Long levelId, Long uid);

    Long getTaskCountByLevel(Long levelId);
}
