
package com.kxmall.wechat.service;

import cn.hutool.core.util.StrUtil;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.wechat.WxMpConfiguration;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName 微信公众号模板通知
 * @Date 2020/6/27
 **/
@Slf4j
@Service
public class WeixinTemplateService {

    /**
     * 发送工资
     *
     * @param phone
     * @param sendId
     */
    public void sendSalary(String name, String sendTime, String salaryAmt, String phone, Long sendId) {

        String openid = this.getUserOpenid(phone);

        if (StrUtil.isBlank(openid)) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("first", "发工资啦！");
        //订单号
        map.put("keyword1", name);
        map.put("keyword2", sendTime);
        map.put("keyword3", salaryAmt);
        //map.put("remark", ShopConstants.YSHOP_WECHAT_PUSH_REMARK);
        String tempId = "CirSf1QdfcIYsKzGGeNmCdJhxU8i098Oay64CiI6tRY";
        if (StrUtil.isNotBlank(tempId)) {
            this.sendWxMpTemplateMessage(openid, tempId, this.getSiteUrl() + "#/pages/login/verify?sendId=" + sendId, map);
        }
    }

    /**
     * 构建微信模板通知
     *
     * @param openId     单号
     * @param templateId 模板id
     * @param url        跳转url
     * @param map        map内容
     * @return String
     */
    private String sendWxMpTemplateMessage(String openId, String templateId, String url, Map<String, String> map) {
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
            .toUser(openId)
            .templateId(templateId)
            .url(url)
            .build();
        map.forEach((k, v) -> {
            templateMessage.addData(new WxMpTemplateData(k, v, "#000000"));
        });
        String msgId = null;
        WxMpService wxService = WxMpConfiguration.getWxMpService();
        try {
            msgId = wxService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            log.error(e.getMessage(),e);
            throw new ServiceException(e.getMessage());
        }
        return msgId;
    }

    /**
     * 返回H5 url
     *
     * @return url
     */
    private String getSiteUrl() {
//        String apiUrl = RedisUtils.getCacheObject(ShopKeyUtils.getSiteUrl());
//        if (StrUtil.isBlank(apiUrl)) {
//            return "http://h5.qyygfw.cn/";
//        }
//        return apiUrl;
        return "";
    }

    /**
     * 获取openid
     *
     * @param phone
     * @return String
     */
    private String getUserOpenid(String phone) {
//        SysUserEmployee sysUserEmployee = sysUserEmployeeService.selectUserByPhonenumber(phone);
//        if (sysUserEmployee == null) {
//            throw new ServiceException("手机号不存在！");
//        }
//        if (StrUtil.isBlank(sysUserEmployee.getOpenId())) {
//            throw new ServiceException("用户未绑定微信公众号！");
//        }
//        return sysUserEmployee.getOpenId();
        return "";
    }


}
