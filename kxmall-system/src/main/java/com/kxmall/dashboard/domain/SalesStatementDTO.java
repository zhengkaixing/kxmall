package com.kxmall.dashboard.domain;

import lombok.Data;

import java.util.List;

/**
 * @author: kaxmll
 * @date: 2020/3/9
 * @time: 23:23
 */

@Data
public class SalesStatementDTO {

    private Long id;
    /**
     * 一级类目ID
     */
    private Long categoryId;

    /**
     * 类目
     */
    private String categoryTitle;
    /**
     * 总销量
     */
    private Long totalSales;
    /**
     * 总销售额
     */
    private Float totalSalesVolume;
    /**
     * top详情
     */
    private List<SalesTopDTO> salesTopDTOs;


}
