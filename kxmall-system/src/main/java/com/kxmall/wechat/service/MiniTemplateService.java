package com.kxmall.wechat.service;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.user.domain.KxUser;
import com.kxmall.user.mapper.KxUserMapper;
import com.kxmall.wechat.domain.vo.SocialWxaSubscribeMessageSendReqVo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.collection.CollUtil.findOne;
import static com.kxmall.common.utils.collection.MapUtils.findAndThen;
import static com.kxmall.wechat.WxMaConfiguration.getWxMaService;


/**
 * @ClassName 小程序模板通知
 * @Date 2020/6/27
 **/
@Slf4j
@Service
public class MiniTemplateService {



    @Resource
    private KxUserMapper kxUserMapper;

    /**
     * 订阅消息跳转小程序类型
     *
     * 1. developer：开发版
     * 2. trial：体验版
     * 3. formal：正式版
     */
    @Value("${com.kxmall.wxa-subscribe-message.miniprogram-state:formal}")
    public String miniprogramState;



    public Boolean sendWxaSubscribeMessage(SocialWxaSubscribeMessageSendReqVo reqDTO) {
        // 1.1 获得订阅模版列表
        List<TemplateInfo> templateList = getSelf().getSubscribeTemplateList();
        if (CollUtil.isEmpty(templateList)) {
            log.warn("[sendSubscribeMessage][reqDTO({}) 发送订阅消息失败，原因：没有找到订阅模板]", reqDTO);
            return false;
        }
        // 1.2 获得需要使用的模版
        TemplateInfo template = findOne(templateList, item ->
                ObjUtil.equal(item.getTitle(), reqDTO.getTemplateTitle()));
        if (template == null) {
            log.warn("[sendWxaSubscribeMessage][reqDTO({}) 发送订阅消息失败，原因：没有找到订阅模板]", reqDTO);
            return false;
        }

        // 2. 获得社交用户
        KxUser kxUser = kxUserMapper.selectById(reqDTO.getUserId());
        if (StrUtil.isBlankIfStr(kxUser.getOpenId())) {
            log.warn("[sendWxaSubscribeMessage][reqDTO({}) 发送订阅消息失败，原因：会员 openid 缺失]", reqDTO);
            return false;
        }

        // 3. 发送订阅消息
        getSelf().sendSubscribeMessage(reqDTO, template.getPriTmplId(), kxUser.getOpenId());
        return true;
    }


    public void sendSubscribeMessage(SocialWxaSubscribeMessageSendReqVo reqDTO, String templateId, String openId) {
        WxMaService service = getWxMaService();
        try {
            WxMaSubscribeService subscribeService = service.getSubscribeService();
            subscribeService.sendSubscribeMsg(buildMessageSendReqDTO(reqDTO, templateId, openId));
        } catch (WxErrorException e) {
            log.error("[sendSubscribeMessage][reqVO({}) templateId({}) openId({}) 发送小程序订阅消息失败]", reqDTO, templateId, openId, e);
            throw new ServiceException("发送小程序订阅消息失败!原因:"+e.getMessage());
        }
    }


    /**
     * 构建发送消息请求参数
     *
     * @param reqDTO     请求
     * @param templateId 模版编号
     * @param openId     会员 openId
     * @return 微信小程序订阅消息请求参数
     */
    private WxMaSubscribeMessage buildMessageSendReqDTO(SocialWxaSubscribeMessageSendReqVo reqDTO,
                                                        String templateId, String openId) {
        // 设置订阅消息基本参数
        WxMaSubscribeMessage subscribeMessage = WxMaSubscribeMessage.builder().lang(WxMaConstants.MiniProgramLang.ZH_CN)
                .miniprogramState(miniprogramState).templateId(templateId).toUser(openId).page(reqDTO.getPage()).build();
        // 设置具体消息参数
        Map<String, String> messages = reqDTO.getMessages();
        if (CollUtil.isNotEmpty(messages)) {
            reqDTO.getMessages().keySet().forEach(key -> findAndThen(messages, key, value ->
                    subscribeMessage.addData(new WxMaSubscribeMessage.MsgData(key, value))));
        }
        return subscribeMessage;
    }


    @Cacheable(cacheNames = "wxa_subscribe_template", key = "#userType", condition = "#result != null")
    public List<TemplateInfo> getSubscribeTemplateList() {
        WxMaService service = getWxMaService();
        try {
            WxMaSubscribeService subscribeService = service.getSubscribeService();
            return subscribeService.getTemplateList();
        } catch (WxErrorException e) {
            log.error("[getSubscribeTemplate][获得小程序订阅消息模版]", e);
            throw new ServiceException("获得小程序订阅消息模版失败！原因:"+e.getMessage());
        }
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private MiniTemplateService getSelf() {
        return SpringUtil.getBean(getClass());
    }
}
