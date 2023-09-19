package com.kxmall.order.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单对象 kx_store_order
 *
 * @author kxmall
 * @date 2023-02-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_order")
@Builder
public class KxStoreOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 订单号
     */
    private String orderId;

    /**
     * 用户id
     */
    private Long uid;
    /**
     * 用户姓名
     */
    private String realName;
    /**
     * 用户电话
     */
    private String userPhone;
    /**
     * 详细地址
     */
    private String userAddress;
    /**
     * 购物车id
     */
    private String cartId;
    /**
     * 运费金额
     */
    private BigDecimal freightPrice;
    /**
     * 订单商品总数
     */
    private Long totalNum;
    /**
     * 订单总价
     */
    private BigDecimal totalPrice;
    /**
     * 原始总价
     */
    private BigDecimal originalTotalPrice;
    /**
     * 邮费
     */
    private BigDecimal totalPostage;
    /**
     * 实际支付金额
     */
    private BigDecimal payPrice;
    /**
     * 支付邮费
     */
    private BigDecimal payPostage;
    /**
     * 抵扣金额
     */
    private BigDecimal deductionPrice;
    /**
     * 优惠券id
     */
    private Long couponId;
    /**
     * 优惠券金额
     */
    private BigDecimal couponPrice;
    /**
     * 支付时间
     */
    private Date payTime;

    /**
     *
     */
    private Integer status;
    /**
     * 0 未退款 1 申请中 2 已退款
     */
    private Integer refundStatus;
    /**
     * 退款图片
     */
    private String refundReasonWapImg;
    /**
     * 退款用户说明
     */
    private String refundReasonWapExplain;
    /**
     * 退款时间
     */
    private Date refundReasonTime;
    /**
     * 前台退款原因
     */
    private String refundReasonWap;
    /**
     * 不退款的理由
     */
    private String refundReason;
    /**
     * 退款金额
     */
    private BigDecimal refundPrice;
    /**
     * 快递公司编号
     */
    private String deliverySn;
    /**
     * 快递名称/送货人姓名
     */
    private String deliveryName;
    /**
     * 发货类型
     */
    private String deliveryType;
    /**
     * 快递单号/手机号
     */
    private String deliveryId;
    /**
     * 消费赚取积分
     */
    private BigDecimal gainIntegral;
    /**
     * 使用积分
     */
    private BigDecimal useIntegral;
    /**
     * 实际支付积分
     */
    private BigDecimal payIntegral;
    /**
     * 给用户退了多少积分
     */
    private BigDecimal backIntegral;
    /**
     * 备注
     */
    private String mark;
    /**
     * 是否删除
     */
    private Integer isDel;
    /**
     * 管理员备注
     */
    private String remark;
    /**
     * 商户ID
     */
    private Integer merId;
    /**
     *
     */
    private Integer isMerCheck;
    /**
     * 拼团产品id0一般产品
     */
    private Long combinationId;
    /**
     * 拼团id 0没有拼团
     */
    private Long pinkId;
    /**
     * 成本价
     */
    private BigDecimal cost;
    /**
     * 秒杀产品ID
     */
    private Long seckillId;
    /**
     * 砍价id
     */
    private Long bargainId;
    /**
     * 核销码
     */
    private String verifyCode;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 配送方式 1=配送 ，2=门店自提
     */
    private Integer shippingType;
    /**
     * 支付渠道(0微信公众号1微信小程序)
     */
    private String isChannel;
    /**
     *
     */
    private Integer isRemind;
    /**
     *
     */
    private Integer isSystemDel;

    /**
     * 预计时间
     */
    private Date predictDate;


    /**
     * 预计时间
     */
    private String predictTime;



    private String province;

    private String city;

    private String county;

    private String payId;

    private String payChannel;

}
