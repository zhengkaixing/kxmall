package com.kxmall.common.core.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/8/5
 */
@Data
@Builder
public class PrintBean {

    private String storageName;

    private String orderNo;

    private BigDecimal totalMoney;

    private String remark;

    private String address;

    private String name;

    private String phone;

    private Date orderTime;

    /**
     * 预计到达时间段
     */
    private String predictTime;


    private List<PrintItemBean> itemBeans;

    /**
     * 订单方式 0 配送 1 自提
     */
    private Integer orderType;


}
