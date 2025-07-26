package com.kxmall.web.controller.decorate;

import cn.dev33.satoken.annotation.SaIgnore;
import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.R;
import com.kxmall.decorate.domain.vo.KxPageTemplateVo;
import com.kxmall.web.controller.decorate.service.IKxAppPageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页页面模板
 *
 * @author kxmall
 * @date 2023-11-05
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/decorate/app")
public class KxAppPageTemplateController extends BaseController {

    private final IKxAppPageTemplateService iKxAppPageTemplateService;


    /**
     * 获取首页页面模板启用模板
     *
     */
    @SaIgnore
    @GetMapping("/pageTemplate")
    public R<KxPageTemplateVo> pageTemplate() {
        return R.ok(iKxAppPageTemplateService.pageTemplate());
    }


}
