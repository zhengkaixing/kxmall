package com.kxmall.order.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kxmall.order.domain.KxStoreOrderProduct;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 售后订单视图对象 kx_store_after_sales
 *
 * @author kxmall
 * @date 2024-11-23
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreAfterSalesVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 订单号
     */
    @ExcelProperty(value = "订单号")
    private String orderCode;

    /**
     * 退款金额
     */
    @ExcelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    /**
     * 服务类型0仅退款1退货退款
     */
    @ExcelProperty(value = "服务类型0仅退款1退货退款")
    private Integer serviceType;

    /**
     * 申请原因
     */
    @ExcelProperty(value = "申请原因")
    private String reasons;

    /**
     * 说明
     */
    @ExcelProperty(value = "说明")
    private String explains;

    /**
     * 说明图片->多个用逗号分割
     */
    @ExcelProperty(value = "说明图片->多个用逗号分割")
    private String explainImg;

    /**
     * 物流公司编码
     */
    @ExcelProperty(value = "物流公司编码")
    private String shipperCode;

    /**
     * 物流单号
     */
    @ExcelProperty(value = "物流单号")
    private String deliverySn;

    /**
     * 物流名称
     */
    @ExcelProperty(value = "物流名称")
    private String deliveryName;

    /**
     * 状态 0已提交等待平台审核 1平台已审核 等待用户发货/退款 2 用户已发货 3退款成功
     */
    @ExcelProperty(value = "状态 0已提交等待平台审核 1平台已审核 等待用户发货/退款 2 用户已发货 3退款成功")
    private Integer state;

    /**
     * 售后状态-0正常1用户取消2商家拒绝
     */
    @ExcelProperty(value = "售后状态-0正常1用户取消2商家拒绝")
    private Integer salesState;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private Long userId;

    /**
     * 商家收货人
     */
    @ExcelProperty(value = "商家收货人")
    private String consignee;

    /**
     * 商家手机号
     */
    @ExcelProperty(value = "商家手机号")
    private String phoneNumber;

    /**
     * 商家地址
     */
    @ExcelProperty(value = "商家地址")
    private String address;

    /**
     * 删除状态
     */
    @ExcelProperty(value = "删除状态")
    private Integer isDel;


    List<KxStoreOrderProduct> cartInfo;

    private Date createTime;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    /**
     * 审核失败时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditFailedTime;

    /**
     * 撤销时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date revocationTime;

    /**
     * 关闭售后时间
     */
    private Date closeAfterSaleTime;

}
