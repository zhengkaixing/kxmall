package com.kxmall.web.controller.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kxmall.user.domain.KxUserTaskFinish;

public interface IKxAppUserTaskFinishService extends IService<KxUserTaskFinish> {

    void setFinish(Long uid, Long taskId);
}
