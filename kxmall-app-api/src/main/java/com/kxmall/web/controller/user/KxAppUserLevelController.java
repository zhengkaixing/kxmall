package com.kxmall.web.controller.user;

import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.R;
import com.kxmall.user.domain.vo.TaskDto;
import com.kxmall.user.domain.vo.UserLevelDto;
import com.kxmall.web.controller.user.service.IKxAppUserLevelService;
import com.kxmall.web.controller.user.service.IKxAppUserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户等级（App）
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/userLevel")
public class KxAppUserLevelController extends BaseAppController {

    private final IKxAppUserLevelService iKxUserLevelService;
    private final IKxAppUserTaskService iKxUserTaskService;

    /**
     * 会员等级列表
     */
    @GetMapping("/grade")
    public R<UserLevelDto> getLevelInfo() {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(iKxUserLevelService.getLevelInfo(userId));
    }

    /**
     * 获取等级任务
     */
    @GetMapping("/task/{id}")
    public R<TaskDto> getTask(@PathVariable Long id) {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(iKxUserTaskService.getTaskList(id, userId));
    }

    /**
     * 检测用户是否可以成为会员
     */
    @GetMapping("/detection")
    public R<String> detection() {
        Long userId = getAppLoginUser().getUserId();
        boolean res = iKxUserLevelService.setLevelComplete(userId);
        if (res) {
            return R.ok("升级成功!");
        } else {
            return R.ok("还不符合升级条件哦!");
        }
    }
}
