
package com.kxmall.wechat.handler;

import com.alibaba.fastjson.JSONObject;
import com.kxmall.storage.domain.KxStorage;
import com.kxmall.storage.mapper.KxStorageMapper;
import com.kxmall.wechat.builder.TextBuilder;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class ScanHandler extends AbstractHandler {

    private static Logger logger = LoggerFactory.getLogger(ScanHandler.class);

    @Resource
    private KxStorageMapper storageMapper;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        String openId = wxMpXmlMessage.getFromUser();
        String eventKey = wxMpXmlMessage.getEventKey();

        logger.info("==扫码事件进行记录==" + eventKey + "=====" + openId);
        if (StringUtils.isEmpty(eventKey)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(eventKey);
        Long storageId = jsonObject.getLong("storageId");
        //店家消息处理部分
        if (!ObjectUtils.isEmpty(storageId)) {
            KxStorage update = new KxStorage();
            update.setId(storageId);
            // 扫码事件处理
            if (storageMapper.updateById(update)<=0) {
                return new TextBuilder().build("商家订阅失败！", wxMpXmlMessage, wxMpService);
            }
        }
        return new TextBuilder().build("订阅成功！", wxMpXmlMessage, wxMpService);
    }
}
