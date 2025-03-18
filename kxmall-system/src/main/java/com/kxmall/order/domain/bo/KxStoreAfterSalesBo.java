package com.kxmall.order.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 售后订单业务对象 kx_store_after_sales
 *
 * @author kxmall
 * @date 2024-11-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxStoreAfterSalesBo extends BaseEntity {

    /**
     * 
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderCode;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 服务类型0仅退款1退货退款
     */
    private Integer serviceType;

    /**
     * 申请原因
     */
    private String reasons;

    /**
     * 说明
     */
    private String explains;

    /**
     * 说明图片->多个用逗号分割
     */
    private String explainImg;

    /**
     * 物流公司编码
     */
    private String shipperCode;

    /**
     * 物流单号
     */
    private String deliverySn;

    /**
     * 物流名称
     */
    private String deliveryName;

    /**
     * 状态 0已提交等待平台审核 1平台已审核 等待用户发货/退款 2 用户已发货 3退款成功
     */
    private Integer state;

    /**
     * 售后状态-0正常1用户取消2商家拒绝
     */
    private Integer salesState;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商家收货人
     */
    private String consignee;

    /**
     * 商家手机号
     */
    private String phoneNumber;

    /**
     * 商家地址
     */
    private String address;

    /**
     * 删除状态
     */
    private Integer isDel;


    /**
     * 类型
     */
    private Integer type;

    /**
     * 查询时间
     */
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
