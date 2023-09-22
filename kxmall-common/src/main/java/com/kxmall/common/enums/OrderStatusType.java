package com.kxmall.common.enums;

/**
 * Created by admin on 2019/2/13.
 */
public enum OrderStatusType {
    UNPAY(10, "未付款"),
    GROUP_SHOP_WAIT(12,"等待团购活动结束"),

    WAIT_PREPARE_GOODS(14, "等待配货"),

    PREPARING_GOODS(16, "配货中"),

    WAIT_STOCK(20, "待出库"),

    WAIT_CONFIRM(30, "待收货"),

    TRANS_ABNORMAL(32, "配送异常"),

    TIME_OUT(34, "配送超时"),

    /**
     * 前台显示待评价的单子在商户后台的显示状态当中表示已完成
     */
    WAIT_APPRAISE(40, "待评价"),
    COMPLETE(50, "已完成"),
    REFUNDING(60, "退款中"),
    REFUNDED(70, "已退款"),
    CANCELED(80, "已取消"),
    CANCELED_SYS(90, "已取消（系统）");

    OrderStatusType(int code, String msg) {
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


    /**
     * 判断定订单是否可退款
     * @return
     */
    public static boolean refundable(int orderStauts) {
        if (orderStauts >= WAIT_PREPARE_GOODS.getCode() && orderStauts <= WAIT_STOCK.getCode()) {
            return true;
        } else {
            return false;
        }
    }

}
