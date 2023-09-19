package com.kxmall.storage.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 定位最近仓库DTO传输类
 * @author: kxmall
 * @date: 2020/02/22 17:37
 **/
@Data
public class RecentlyStorageVo implements Serializable {

    /*** 仓库主键ID*/
    private Long id;

    /*** 最近是否有仓库*/
    private Boolean haveStorage;

    /*** 最近仓库是否营业*/
    private Boolean businessState;

    /*** 营业起始时间*/
    private String businessStartTime;

    /*** 营业结束时间*/
    private String businessStopTime;

    /*** 配送起始时间*/
    private String deliveryStartTime;

    /*** 配送结束时间*/
    private String deliveryStopTime;

    /**
     * 距离
     */
    private BigDecimal distance = BigDecimal.ZERO;

}
