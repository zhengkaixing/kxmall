package com.kxmall.common.enums;

/**
 *
 */
public enum PayChannelType {
    WEPAY("WX", "微信支付"),
    ALIPAY("ALI", "支付宝"),
    BALANCE("BALANCE", "余额"),
    OFFLINE("OFFLINE", "线下支付"),
    INTEGRAL("INTEGRAL", "积分兑换"),
    ;

    PayChannelType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
