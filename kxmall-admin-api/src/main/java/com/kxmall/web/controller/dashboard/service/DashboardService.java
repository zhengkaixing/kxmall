package com.kxmall.web.controller.dashboard.service;

import com.kxmall.dashboard.domain.OrderTimeDataDTO;
import com.kxmall.dashboard.domain.SalesStatementDTO;
import com.kxmall.dashboard.domain.UserStatementDTO;

import java.util.List;
import java.util.Set;


/**
 * 首页数据服务
 */
public interface DashboardService {


    /**
     * 用户数量统计
     *
     * @param
     * @return
     */
    List<UserStatementDTO> countUser();

    /**
     * 销售统计
     *
     * @param storageId
     * @param storagePermission
     * @return
     */
    List<SalesStatementDTO> getSalesStatement(Long storageId, Set<Long> storagePermission);

    /**
     * 近两日销售统计
     *
     * @param storageId
     * @param storagePermission
     * @return
     */
    List<SalesStatementDTO> getTodayAndYesterdaySales(Long storageId, Set<Long> storagePermission);

    /**
     * 按小时统计销量
     *
     * @param storageId
     * @param storagePermission
     * @return
     */
    List<SalesStatementDTO> getSalesByHour(Long storageId, Set<Long> storagePermission);


    /**
     * 首页订单/用户等统计
     * @return OrderTimeDataDto
     * @param storageId
     */
    OrderTimeDataDTO getCount(Long storageId);


}
