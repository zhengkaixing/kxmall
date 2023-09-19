package com.kxmall.common.enums;

public enum GoodsInStockType {
    TO_BE_FOR_STOCK(0, "待入库"),
    IN_FOR_STOCK(1, "已入库"),
    ;

    GoodsInStockType(int code, String msg) {
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
