package com.kxmall.dashboard.domain;

import lombok.Data;

/**
 * @author: kxmall
 * @date: 2020/3/9
 * @time: 23:23
 */

@Data
public class SalesTopDTO {

    /**
     * 名称
     */
    private String title;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 商品规格ID
     */
    private Long productAttrId;
    /**
     * 销量
     */
    private Long totalSales;
    /**
     * 销售额
     */
    private Float totalSalesVolume;


}
