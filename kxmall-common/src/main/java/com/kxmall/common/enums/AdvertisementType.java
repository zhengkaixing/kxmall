package com.kxmall.common.enums;


/**
 *
 */
public enum AdvertisementType {

    /**
     * 1、首页轮播
     * 2、首页图片
     * 3、分类图片
     * 4、精品类目
     * 5、活动弹窗
     * 6、个人中心
     * 7、十大类列表
     * 8、特价列表
     */
    CAROUSEL(1, 3, "首页轮播"),
    CATEGORY_PICK(2, 1, "分类精选"),
    BANNER(3,3, "广告横幅"),
    HOME_BUTTON(4, 1, "首页5按钮"),
    POPUP_WINDOW(5, 1, "活动弹窗"),

    MAIN_PIC(6, 3, "首页图片"),
    CLASSIFICATION(7,3, "分类图片"),
    PERSONAL_CENTER(8, 3, "个人中心"),
    ON_SALE(10, 2, "特价列表");



    AdvertisementType(int code, int unionType, String msg) {
        this.code = code;
        this.unionType = unionType;
        this.msg = msg;
    }

    private int code;

    /**
     * unionType 1.类目 2.商品  1+2=3
     */
    private int unionType;

    private String msg;

    public int getCode() {
        return code;
    }

    public int getUnionType() {
        return unionType;
    }

    public String getMsg() {
        return msg;
    }

}

