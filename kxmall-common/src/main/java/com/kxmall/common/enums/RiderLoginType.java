package com.kxmall.common.enums;

/**
 * Created by admin on 2022/10/23.
 */
public enum RiderLoginType {
    REGISTER(0, "手机注册"),
    MP_WEIXIN(1, "WX小程序登录"),
    APP_WEIXIN(2, "WX第三方登录"),
    H5_WEIXIN(3, "H5微信登录"),
    PHONE_WEIXIN(4, "手机号登录");

    RiderLoginType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static boolean contains(Integer loginType) {
        if (loginType == null) {
            return false;
        }
        for (RiderLoginType type : values()) {
            if (type.getCode() == loginType) {
                return true;
            }
        }
        return false;
    }


}
