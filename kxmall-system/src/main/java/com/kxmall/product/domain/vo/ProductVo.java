package com.kxmall.product.domain.vo;


import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductVo {

    /**
     * 商品id
     */
    private Long id;


    /**
     * 商品图片
     */
    private JSONArray image;

    /**
     * 轮播图
     */
    private JSONArray sliderImage;

    /**
     * 商品名称
     */
    private String storeName;

    /**
     * 商品简介
     */
    private String storeInfo;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 商品条码（一维码）
     */
    private String barCode;

    /**
     * 分类id
     */
    private String cateId;

    /**
     * 商品价格
     */
    private Double price;


    /**
     * 市场价
     */
    private Double otPrice;

    /**
     * 邮费
     */
    private Double postage;

    /**
     * 单位名
     */
    private String unitName;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 销量
     */
    private Long sales;

    /**
     * 库存
     */
    private Long stock;

    /**
     * 状态（0：未上架，1：上架）
     */
    private Integer isShow;

    /**
     * 是否热卖
     */
    private Integer isHot;

    /**
     * 是否优惠
     */
    private Integer isBenefit;

    /**
     * 是否精品
     */
    private Integer isBest;

    /**
     * 是否新品
     */
    private Integer isNew;

    /**
     * 商品描述
     */
    private String description;


    /**
     * 是否包邮
     */
    private Integer isPostage;


    /**
     * 获得积分
     */
    private Double giveIntegral;

    /**
     * 成本价
     */
    private Double cost;


    /**
     * 是否优品推荐
     */
    private Integer isGood;

    /**
     * 是否单独分佣
     */
    private Integer isSub;

    /**
     * 是否开启啊积分兑换
     */
    private Integer isIntegral;

    /**
     * 虚拟销量
     */
    private Long ficti;


    /**
     * 运费模板ID
     */
    private Long tempId;

    /**
     * 规格 0单 1多
     */
    private Integer specType;

    private ProductFormatVo attr;

    private List<FromatDetailVo> items;

    private List<ProductFormatVo> attrs;


}
