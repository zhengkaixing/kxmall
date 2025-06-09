package com.kxmall.finance.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SalesReportDto {

    /**
     * 仓库名称
     */
    private String storageName;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;
    /**
     * 销售量
     */
    private Long totalSalesVolume;
    /**
     * 总销售额
     */
    private BigDecimal totalSalesAmount;
    /**
     * 优惠券优惠金额
     */
    private BigDecimal totalCouponAmount;
    /**
     * 实际支付金额
     */
    private BigDecimal actualPaymentAmount;


}

