package com.kxmall.web.controller.user;

import com.kxmall.common.annotation.RepeatSubmit;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.user.domain.bo.KxUserSignBo;
import com.kxmall.user.domain.vo.KxUserSignVo;
import com.kxmall.user.domain.vo.KxUserVo;
import com.kxmall.web.controller.user.service.IKxAppUserSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 签到记录
 *
 * @author kxmall
 * @date 2024-08-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/app/userSign")
public class KxAppUserSignController extends BaseAppController {

    private final IKxAppUserSignService iKxUserSignService;

    /**
     * 查询签到记录列表
     */
    @GetMapping("/list")
    public TableDataInfo<KxUserSignVo> list(KxUserSignBo bo, PageQuery pageQuery) {
        return iKxUserSignService.queryPageList(bo, pageQuery);
    }

    /**
     * 新增签到记录
     */
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxUserSignBo bo) {
        return toAjax(iKxUserSignService.insertByBo(bo));
    }


    /**
     * 查询某一用户收藏记录
     */
    @GetMapping("/getSignInUser")
    public R<KxUserVo> getSignInUser() {
        Long userId = getAppLoginUser().getUserId();
        KxUserVo signInUser = iKxUserSignService.getSignInUser(userId);
        return R.ok(signInUser);
    }

    /**
     * 开始签到
     */
    @RepeatSubmit()
    @GetMapping("/sign/integral")
    public R<Map<String,Object>> signIntegral(){

        Long userId = getAppLoginUser().getUserId();
        Long integral = iKxUserSignService.sign(userId);
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("integral",integral);
        return R.ok("签到获得" + integral + "积分",map);
    }

}
