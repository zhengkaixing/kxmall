package com.kxmall.order.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ExcelIgnoreUnannotated
public class KxStoreOrderProductVo {

    private static final long serialVersionUID = 1L;

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

}
