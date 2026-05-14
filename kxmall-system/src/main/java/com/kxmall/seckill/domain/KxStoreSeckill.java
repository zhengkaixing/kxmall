package com.kxmall.seckill.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品秒杀对象 kx_store_seckill
 *
 * @author kxmall
 * @date 2024-05-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_seckill")
public class KxStoreSeckill extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品秒杀产品表id
     */
    @TableId(value = "id")
    private Long id;

    private Long storageId;

    /**
     * 商品id
     */
    private Long productId;
    /**
     * 推荐图
     */
    private String image;
    /**
     * 轮播图
     */
    private String images;
    /**
     * 活动标题
     */
    private String title;
    /**
     * 简介
     */
    private String info;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 成本
     */
    private BigDecimal cost;
    /**
     * 原价
     */
    private BigDecimal otPrice;
    /**
     * 返多少积分
     */
    private BigDecimal giveIntegral;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 销量
     */
    private Integer sales;
    /**
     * 单位名
     */
    private String unitName;
    /**
     * 邮费
     */
    private BigDecimal postage;
    /**
     * 内容
     */
    private String description;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String stopTime;
    /**
     * 产品状态 1启动  0停用
     */
    private Integer status;
    /**
     * 是否包邮
     */
    private Integer isPostage;
    /**
     * 热门推荐
     */
    private Integer isHot;
    /**
     * 最多秒杀几个
     */
    private Long num;
    /**
     * 显示
     */
    private Integer isShow;
    /**
     * 时间段id
     */
    private Integer timeId;
    /**
     * 规格 0单 1多
     */
    private Integer specType;
    /**
     * 运费模板id
     */
    private Integer tempId;
    /**
     *
     */
    private Integer isDel;

    private String seckillStartTime;

    private String seckillStopTime;
}
