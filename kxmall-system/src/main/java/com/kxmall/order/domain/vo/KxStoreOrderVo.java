package com.kxmall.order.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kxmall.common.annotation.ExcelDictFormat;
import com.kxmall.common.convert.ExcelDictConvert;
import com.kxmall.order.domain.KxStoreOrderProduct;
import com.kxmall.user.domain.vo.KxUserVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 订单视图对象 kx_store_order
 *
 * @author kxmall
 * @date 2023-02-15
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreOrderVo {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @ExcelProperty(value = "订单ID")
    private Long id;

    /**
     * 订单号
     */
    @ExcelProperty(value = "订单号")
    private String orderId;

    /**
     * 额外订单号
     */
    @ExcelProperty(value = "额外订单号")
    private String extendOrderId;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private Long uid;

    /**
     * 用户姓名
     */
    @ExcelProperty(value = "用户姓名")
    private String realName;

    /**
     * 用户电话
     */
    @ExcelProperty(value = "用户电话")
    private String userPhone;

    /**
     * 详细地址
     */
    @ExcelProperty(value = "详细地址")
    private String userAddress;

    /**
     * 购物车id
     */
    @ExcelProperty(value = "购物车id")
    private String cartId;

    /**
     * 运费金额
     */
    @ExcelProperty(value = "运费金额")
    private BigDecimal freightPrice;

    /**
     * 订单商品总数
     */
    @ExcelProperty(value = "订单商品总数")
    private Long totalNum;

    /**
     * 订单总价
     */
    @ExcelProperty(value = "订单总价")
    private BigDecimal totalPrice;

    /**
     * 邮费
     */
    @ExcelProperty(value = "邮费")
    private BigDecimal totalPostage;

    /**
     * 实际支付金额
     */
    @ExcelProperty(value = "实际支付金额")
    private BigDecimal payPrice;

    /**
     * 支付邮费
     */
    @ExcelProperty(value = "支付邮费")
    private BigDecimal payPostage;

    /**
     * 抵扣金额
     */
    @ExcelProperty(value = "抵扣金额")
    private BigDecimal deductionPrice;

    /**
     * 优惠券id
     */
    @ExcelProperty(value = "优惠券id")
    private Long couponId;

    /**
     * 优惠券金额
     */
    @ExcelProperty(value = "优惠券金额")
    private BigDecimal couponPrice;

    /**
     * 支付时间
     */
    @ExcelProperty(value = "支付时间")
    private Date payTime;

    /**
     * 支付方式
     */
    @ExcelProperty(value = "支付方式")
    private String payType;

    /**
     * 订单状态（-1 : 申请退款 -2 : 退货成功 0：待发货；1：待收货；2：已收货；3：已完成；-1：已退款）
     */
    @ExcelProperty(value = "订单状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "-=1,:=,申=请退款,-=2,:=,退=货成功,0=：待发货；1：待收货；2：已收货；3：已完成；-1：已退款")
    private Integer status;

    /**
     * 0 未退款 1 申请中 2 已退款
     */
    @ExcelProperty(value = "0 未退款 1 申请中 2 已退款")
    private Integer refundStatus;

    /**
     * 退款图片
     */
    @ExcelProperty(value = "退款图片")
    private String refundReasonWapImg;

    /**
     * 退款用户说明
     */
    @ExcelProperty(value = "退款用户说明")
    private String refundReasonWapExplain;

    /**
     * 退款时间
     */
    @ExcelProperty(value = "退款时间")
    private Date refundReasonTime;

    /**
     * 前台退款原因
     */
    @ExcelProperty(value = "前台退款原因")
    private String refundReasonWap;

    /**
     * 不退款的理由
     */
    @ExcelProperty(value = "不退款的理由")
    private String refundReason;

    /**
     * 退款金额
     */
    @ExcelProperty(value = "退款金额")
    private BigDecimal refundPrice;

    /**
     * 快递公司编号
     */
    @ExcelProperty(value = "快递公司编号")
    private String deliverySn;

    /**
     * 快递名称/送货人姓名
     */
    @ExcelProperty(value = "快递名称/送货人姓名")
    private String deliveryName;

    /**
     * 发货类型
     */
    @ExcelProperty(value = "发货类型")
    private String deliveryType;

    /**
     * 快递单号/手机号
     */
    @ExcelProperty(value = "快递单号/手机号")
    private String deliveryId;

    /**
     * 消费赚取积分
     */
    @ExcelProperty(value = "消费赚取积分")
    private BigDecimal gainIntegral;

    /**
     * 使用积分
     */
    @ExcelProperty(value = "使用积分")
    private BigDecimal useIntegral;

    /**
     * 实际支付积分
     */
    @ExcelProperty(value = "实际支付积分")
    private BigDecimal payIntegral;

    /**
     * 给用户退了多少积分
     */
    @ExcelProperty(value = "给用户退了多少积分")
    private BigDecimal backIntegral;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String mark;

    /**
     * 是否删除
     */
    @ExcelProperty(value = "是否删除")
    private Integer isDel;

    /**
     * 唯一id(md5加密)类似id
     */
    @ExcelProperty(value = "唯一id(md5加密)类似id")
    private String unique;

    /**
     * 管理员备注
     */
    @ExcelProperty(value = "管理员备注")
    private String remark;

    /**
     * 商户ID
     */
    @ExcelProperty(value = "商户ID")
    private Integer merId;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Integer isMerCheck;

    /**
     * 拼团产品id0一般产品
     */
    @ExcelProperty(value = "拼团产品id0一般产品")
    private Long combinationId;

    /**
     * 拼团id 0没有拼团
     */
    @ExcelProperty(value = "拼团id 0没有拼团")
    private Long pinkId;

    /**
     * 成本价
     */
    @ExcelProperty(value = "成本价")
    private BigDecimal cost;

    /**
     * 秒杀产品ID
     */
    @ExcelProperty(value = "秒杀产品ID")
    private Long seckillId;

    /**
     * 砍价id
     */
    @ExcelProperty(value = "砍价id")
    private Long bargainId;

    /**
     * 核销码
     */
    @ExcelProperty(value = "核销码")
    private String verifyCode;

    /**
     * 门店id
     */
    @ExcelProperty(value = "门店id")
    private Long storeId;

    /**
     * 配送方式 1=配送 ，2=门店自提
     */
    @ExcelProperty(value = "配送方式 1=配送 ，2=门店自提")
    private Integer shippingType;

    /**
     * 支付渠道(0微信公众号1微信小程序)
     */
    @ExcelProperty(value = "支付渠道(0微信公众号1微信小程序)")
    private Integer isChannel;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Integer isRemind;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Integer isSystemDel;


    private Date createTime;

    private Date updateTime;


    private BigDecimal originalTotalPrice;


    /**
     * 预计时间
     */
    private Date predictDate;


    /**
     * 预计时间
     */
    private String predictTime;

    private String statusName;

    private Integer finishStatus;


    private String payTypeName;

    private String pinkName;


    private String province;

    private String city;

    private String county;

    private KxUserVo kxUserVo;


    private List<KxStoreOrderProductVo> productList;

}
