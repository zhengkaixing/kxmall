package com.kxmall.web.controller.user;

import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.R;
import com.kxmall.user.domain.bo.KxUserBo;
import com.kxmall.user.domain.vo.KxUserVo;
import com.kxmall.web.controller.user.service.IKxAppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * APP端用户信息管理控制器
 * 提供用户信息查询、更新等接口
 *
 * @author 郅兴开源团队-小黑
 * @version 1.0
 * @date 2023/9/1
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/app")
public class KxAppUserController extends BaseAppController {

    /**
     * 用户服务接口
     */
    private final IKxAppUserService kxUserService;

    /**
     * 获取当前登录用户信息
     * 从token中解析用户ID，查询并返回用户详细信息
     *
     * @return R<KxUserVo> 用户信息响应对象
     */
    @GetMapping("/getUser")
    public R<KxUserVo> getUser() {
        // 从token中获取当前登录用户ID
        Long userId = getAppLoginUser().getUserId();
        // 查询用户信息并返回
        return R.ok(kxUserService.queryById(userId));
    }

    /**
     * 更新当前登录用户信息
     * 只能更新当前登录用户自己的信息，用户ID从token中获取
     *
     * @param bo 用户信息业务对象，包含需要更新的字段
     * @return R<Boolean> 更新结果，true表示成功，false表示失败
     */
    @PostMapping("/updateUser")
    public R<Boolean> updateUser(@RequestBody KxUserBo bo) {
        // 从token中获取当前登录用户ID
        Long userId = getAppLoginUser().getUserId();
        // 设置用户ID，确保只能更新自己的信息
        bo.setUid(userId);
        // 执行更新操作
        return R.ok(kxUserService.updateByBo(bo));
    }

}
