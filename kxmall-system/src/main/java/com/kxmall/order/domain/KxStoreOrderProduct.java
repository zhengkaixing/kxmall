package com.kxmall.order.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("kx_store_order_product")
public class KxStoreOrderProduct {

    @TableId(value = "id")
    private Long id;

    /**
     * 规格id
     */
    private Long productAttrId;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 产品名
     */
    private String productTitle;

    /**
     * 规格名
     */
    private String productAttrTitle;

    private String barCode;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 原价
     */
    private BigDecimal otPrice;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 图片
     */
    private String img;

    /**
     * 单位
     */
    private String unitName;


    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
