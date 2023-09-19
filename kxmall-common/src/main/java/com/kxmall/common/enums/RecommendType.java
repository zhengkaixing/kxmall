package com.kxmall.common.enums;

/**
 *
 * @author kaixin
 */
public enum RecommendType {
    /**
     * 今日特价
     */
    CHEAP(1, "今日特价"),
    /**
     * 平价菜场
     */
    PAR(2, "平价菜场"),
    /**
     * 鲜肉蛋禽
     */
    MEAT(3, "鲜肉蛋禽"),
    /**
     * 新鲜水果
     */
    FRUITS(4, "新鲜水果"),
    /**
     * 今日必买
     */
    MUST(5, "今日必买"),
    /**
     * 掌柜推荐
     */
    MANAGER(6, "掌柜推荐"),
    /**
     * 疯狂折扣
     */
    DISCOUNT(7, "疯狂折扣"),
    /**
     * 懒人菜单
     */
    LAZY(8, "懒人菜单"),
    /**
     * 新品推荐
     */
    NEW(9, "新品推荐"),
    /**
     * 爆品囤货
     */
    STORE(10, "爆品囤货"),
    /**
     * 热销推荐
     */
    HOT(11, "热销推荐"),
    ;

    RecommendType(int code, String msg) {
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
