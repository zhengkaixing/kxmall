package com.kxmall.web.controller.social;


import com.kxmall.common.core.domain.R;
import com.kxmall.wechat.service.MiniTemplateService;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 小程序获取首页装修模板
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/social/app")
public class SocialController {

    @Resource
    private MiniTemplateService miniTemplateService;

    /**
     * 获取模板列表
     */
    @GetMapping("/get-subscribe-template-list")
    public R<List<TemplateInfo>> getSubscribeTemplateList() {
        return R.ok(miniTemplateService.getSubscribeTemplateList());
    }

}
