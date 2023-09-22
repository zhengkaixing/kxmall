package com.kxmall.storage.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 商品入库对象 kx_goods_in_stock
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_goods_in_stock")
public class KxGoodsInStock extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 出库id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 仓库id
     */
    private Long storageId;
    /**
     * 入库单号
     */
    private String inStockNumbers;
    /**
     * 0:待入库;1:已入库；
     */
    private Integer states;
    /**
     * 入库人
     */
    private String ingoingPerson;
    /**
     * 入库时间
     */
    private Date ingoingTime;
    /**
     * 备注
     */
    private String remarks;
    /**
     *
     */
    private String outgoingDay;

}
