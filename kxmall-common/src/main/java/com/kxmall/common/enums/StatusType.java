package com.kxmall.common.enums;

/**
 * 通用状态枚举  仅激活 冻结
 *
 * @author kaixin
 * @date 2020/3/10
 */
public enum StatusType {
    /**
     * 下架
     */
    LOCK(0, "冻结"),
    /**
     * 正常
     */
    ACTIVE(1, "激活"),
    ;

    StatusType(int code, String msg) {
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


}
