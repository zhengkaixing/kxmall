
package com.kxmall.wechat.service;

import com.kxmall.storage.domain.KxStorage;
import com.kxmall.storage.mapper.KxStorageMapper;
import com.kxmall.wechat.WxMpConfiguration;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName 微信公众号模板通知
 * @Date 2020/6/27
 **/
@Slf4j
@Service
public class WeixinTemplateService {

    @Resource
    private KxStorageMapper storageMapper;

    /**
     * 客户下单，推送给商家
     *
     * @param name
     * @param sendTime
     * @param price
     * @param orderId
     * @param storeId
     */
    public void sendPlayOrder(String name, String sendTime, String price, String orderId, Long storeId) {

        String openid = this.getStoreOpenid(storeId);
        if (StringUtils.isBlank(openid)) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        //订单号
        map.put("character_string1", orderId);
        map.put("time2", sendTime);
        map.put("amount4", price);
        map.put("thing5", name);
        String tempId = "OHqVq9tYbHPlN9tAXbnJvJO8hKcTzUXGsUCFfqOOLCk";
        if (StringUtils.isNotBlank(tempId)) {
            this.sendWxMpTemplateMessage(openid, tempId, null, map);
        }
    }

    /**
     * 商家点击平台配送，推送给骑手
     *
     * @param sendTime
     * @param price
     * @param orderId
     * @param freightPrice
     * @param riderId
     */
    public void sendPlayRiderOrder(String sendTime, String price, String orderId, String address, String freightPrice, Long riderId) {

        String openid = "";
        if (StringUtils.isBlank(openid)) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        //订单号
        map.put("character_string2", orderId);
        map.put("time5", sendTime);
        map.put("amount3", price);
        map.put("thing15", address);
        map.put("thing11", "配送费：" + freightPrice);
        String tempId = "p2yVaXF9P7aCMtff8BfBX9XkzibOtlU4ufaR5ubScy8";
        if (StringUtils.isNotBlank(tempId)) {
            this.sendWxMpTemplateMessage(openid, tempId, null, map);
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
        String msgId;
        WxMpService wxService = WxMpConfiguration.getWxMpService();
        try {
            msgId = wxService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
        return msgId;
    }


    /**
     * 获取openid
     *
     * @param storeId
     * @return String
     */
    private String getStoreOpenid(Long storeId) {
        KxStorage kxStorage = storageMapper.selectById(storeId);
        if (kxStorage == null) {
            throw new RuntimeException("门店信息不存在！");
        }
        return "";

    }


}
