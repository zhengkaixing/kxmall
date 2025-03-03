package com.kxmall.group.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 团购对象 kx_group_shop
 *
 * @author kxmall
 * @date 2023-10-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_group_shop")
public class KxGroupShop extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     *
     */
    private Long productId;
    /**
     *
     */
    private BigDecimal minPrice;
    /**
     *
     */
    private BigDecimal maxPrice;
    /**
     * 团购开始时间
     */
    private Date startTime;
    /**
     * 团购结束时间
     */
    private Date endTime;
    /**
     * 团购基础人数
     */
    private Long minimumNumber;
    /**
     * 团购已经购买人数
     */
    private Long alreadyBuyNumber;
    /**
     * 团购结束时购买人数未达到基础人数,是否自动退款
     */
    private Integer automaticRefund;
    /**
     * 判断团购商品是否在活动期间
     */
    private Integer status;
    /**
     *
     */
    private Long storageId;

}
