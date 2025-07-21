package com.kxmall.web.controller.decorate.service;

import com.kxmall.decorate.domain.vo.KxPageTemplateVo;

/**
 * 首页页面模板Service接口
 *
 * @author kxmall
 * @date 2023-11-05
 */
public interface IKxAppPageTemplateService {

    /**
     * 首页模板
     *
     * @return
     */
    KxPageTemplateVo pageTemplate();
}
