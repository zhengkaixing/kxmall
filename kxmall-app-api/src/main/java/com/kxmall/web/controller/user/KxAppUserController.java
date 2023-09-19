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
 * app端登录
 *
 * @author kaixin
 * @version 1.0
 * @date 2023/9/1
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/app")
public class KxAppUserController extends BaseAppController {

    private final IKxAppUserService kxUserService;

    /**
     * 查询某一用户收藏记录
     */
    @GetMapping("/getUser")
    public R<KxUserVo> getUser() {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(kxUserService.queryById(userId));
    }


    /**
     * 更新
     */
    @PostMapping("/updateUser")
    public R<Boolean> updateUser(@RequestBody KxUserBo bo) {
        Long userId = getAppLoginUser().getUserId();
        bo.setUid(userId);
        return R.ok(kxUserService.updateByBo(bo));
    }


}
