package com.kxmall.web.controller.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kxmall.user.domain.KxUserTaskFinish;
import com.kxmall.user.mapper.KxUserTaskFinishMapper;
import com.kxmall.web.controller.user.service.IKxAppUserTaskFinishService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class KxAppUserTaskFinishServiceImpl extends ServiceImpl<KxUserTaskFinishMapper, KxUserTaskFinish> implements IKxAppUserTaskFinishService {

    @Override
    public void setFinish(Long uid, Long taskId) {
        KxUserTaskFinish finish = new KxUserTaskFinish();
        finish.setTaskId(taskId);
        finish.setUid(uid);
        finish.setStatus(1);
        finish.setCreateTime(new Date());
        this.save(finish);
    }
}
