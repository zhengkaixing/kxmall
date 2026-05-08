package com.kxmall.web.controller.user.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.enums.ShopCommonEnum;
import com.kxmall.order.mapper.KxStoreOrderMapper;
import com.kxmall.user.domain.KxUserTask;
import com.kxmall.user.domain.KxUserTaskFinish;
import com.kxmall.user.domain.vo.KxUserTaskVo;
import com.kxmall.user.domain.vo.TaskDto;
import com.kxmall.user.mapper.KxUserBillMapper;
import com.kxmall.user.mapper.KxUserTaskFinishMapper;
import com.kxmall.user.mapper.KxUserTaskMapper;
import com.kxmall.web.controller.user.service.IKxAppUserBillService;
import com.kxmall.web.controller.user.service.IKxAppUserTaskFinishService;
import com.kxmall.web.controller.user.service.IKxAppUserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KxAppUserTaskServiceImpl implements IKxAppUserTaskService {

    private final KxUserTaskMapper baseMapper;
    private final KxUserTaskFinishMapper kxUserTaskFinishMapper;
    private final KxStoreOrderMapper storeOrderMapper;
    private final KxUserBillMapper userBillMapper;
    private final IKxAppUserTaskFinishService kxAppUserTaskFinishService;
    private final IKxAppUserBillService kxAppUserBillService;

    @Override
    public TaskDto getTaskList(Long levelId, Long uid) {
        LambdaQueryWrapper<KxUserTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(KxUserTask::getLevelId, levelId == null ? null : levelId.intValue())
                .eq(KxUserTask::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                .orderByAsc(KxUserTask::getSort);

        List<KxUserTaskVo> list = baseMapper.selectVoList(wrapper);

        TaskDto taskDto = new TaskDto();
        taskDto.setList(list);
        taskDto.setReachCount(this.getTaskComplete(levelId, uid));
        taskDto.setTask(this.tidyTask(list, uid));

        return taskDto;
    }

    private List<KxUserTaskVo> tidyTask(List<KxUserTaskVo> task, Long uid) {
        for (KxUserTaskVo taskVo : task) {
            Long count = kxUserTaskFinishMapper.selectCount(new LambdaQueryWrapper<KxUserTaskFinish>()
                    .eq(KxUserTaskFinish::getTaskId, taskVo.getId())
                    .eq(KxUserTaskFinish::getUid, uid));

            if (count > 0) {
                taskVo.setNewNumber(taskVo.getNumber());
                taskVo.setSpeed(100);
                taskVo.setFinish(ShopCommonEnum.IS_FINISH_1.getValue());
                taskVo.setTaskTypeTitle("");
            } else {
                double sumNumber = 0d;
                String title = "";

                switch (taskVo.getTaskType()) {
                    case "SatisfactionIntegral":
                        sumNumber = userBillMapper.sumIntegral(uid);
                        title = "还需要{0}经验";
                        break;
                    case "ConsumptionAmount":
                        sumNumber = storeOrderMapper.sumPrice(uid);
                        title = "还需消费{0}元";
                        break;
                    case "CumulativeAttendance":
                        sumNumber = kxAppUserBillService.cumulativeAttendance(uid);
                        title = "还需签到{0}天";
                        break;
                    default:
                        break;
                }

                if (sumNumber >= taskVo.getNumber()) {
                    kxAppUserTaskFinishService.setFinish(uid, taskVo.getId());
                    taskVo.setFinish(ShopCommonEnum.IS_FINISH_1.getValue());
                    taskVo.setSpeed(100);
                    taskVo.setTaskTypeTitle("");
                    taskVo.setNewNumber(taskVo.getNumber());
                } else {
                    double numdata = NumberUtil.sub(taskVo.getNumber().doubleValue(), sumNumber);
                    taskVo.setTaskTypeTitle(MessageFormat.format(title, numdata));
                    double speed = NumberUtil.div(sumNumber, taskVo.getNumber().doubleValue());
                    taskVo.setSpeed(Double.valueOf(NumberUtil.mul(speed, 100)).intValue());
                    taskVo.setFinish(ShopCommonEnum.IS_FINISH_0.getValue());
                    taskVo.setNewNumber(Double.valueOf(sumNumber).intValue());
                }
            }
        }
        return task;
    }

    @Override
    public Long getTaskComplete(Long levelId, Long uid) {
        LambdaQueryWrapper<KxUserTask> queryWrapper = new LambdaQueryWrapper<KxUserTask>()
                .eq(KxUserTask::getLevelId, levelId == null ? null : levelId.intValue())
                .eq(KxUserTask::getIsShow, ShopCommonEnum.SHOW_1.getValue());

        List<KxUserTask> list = baseMapper.selectList(queryWrapper);

        List<Long> taskIds = list.stream().map(KxUserTask::getId).collect(Collectors.toList());

        if (taskIds.isEmpty()) {
            return 0L;
        }

        return kxUserTaskFinishMapper.selectCount(Wrappers.<KxUserTaskFinish>lambdaQuery()
                .in(KxUserTaskFinish::getTaskId, taskIds)
                .eq(KxUserTaskFinish::getUid, uid));
    }

    @Override
    public Long getTaskCountByLevel(Long levelId) {
        if (levelId == null || levelId <= 0) {
            return 0L;
        }
        LambdaQueryWrapper<KxUserTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KxUserTask::getLevelId, levelId.intValue())
                .eq(KxUserTask::getIsShow, ShopCommonEnum.SHOW_1.getValue());
        return baseMapper.selectCount(queryWrapper);
    }
}
