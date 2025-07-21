package com.kxmall.web.controller.decorate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.decorate.domain.KxPageTemplate;
import com.kxmall.decorate.domain.vo.KxPageTemplateVo;
import com.kxmall.decorate.mapper.KxPageTemplateMapper;
import com.kxmall.web.controller.decorate.service.IKxAppPageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 首页页面模板Service业务层处理
 *
 * @author kxmall
 * @date 2023-11-05
 */
@RequiredArgsConstructor
@Service
public class KxAppPageTemplateServiceImpl implements IKxAppPageTemplateService {

    private final KxPageTemplateMapper baseMapper;


    @Override
    public KxPageTemplateVo pageTemplate() {
        KxPageTemplateVo templateVo = baseMapper.selectVoOne(new LambdaQueryWrapper<KxPageTemplate>().eq(KxPageTemplate::getStatus, 1));
        if (ObjectUtils.isEmpty(templateVo)) {
            throw new ServiceException("首页不存在启用模板，请检查后操作！");
        }
        return templateVo;
    }
}
