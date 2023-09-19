
package com.kxmall.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kxmall
 * 微信公众号模板枚举
 */
@Getter
@AllArgsConstructor
public enum WechatTempateEnum {

    SEND_SUCCESS("send_success", "支付成功通知");

    private String value; //模板编号
    private String desc; //模板id
}
