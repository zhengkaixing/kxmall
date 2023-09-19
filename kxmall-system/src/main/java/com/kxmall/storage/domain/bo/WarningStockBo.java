package com.kxmall.storage.domain.bo;

import lombok.Data;

import java.util.List;

/**
 * 告警查询对象
 * @author kaixin
 * @version 1.0
 * @date 2023/8/29
 */
@Data
public class WarningStockBo {

    private Long storageId;
    private Long categoryId;
    private String name;
    private Integer type;
    private Integer minNum;
    private Integer maxNum;
    /**
     * 预警数量
     */
    private Long num;

    private Boolean showType = false;
    private List<Long> storageIds;

    private List<Long> childrenIds;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品规格id
     */
    private Long productAttrId;

}
