package com.kxmall.storage.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * 前置仓商品对象 kx_stock
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_stock")
public class KxStock extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 商品id
     */
    private Long productId;
    /**
     * 商品规格id
     */
    private Long productAttrId;
    /**
     * 前置仓id
     */
    private Long storageId;
    /**
     * 销售状态1上架0下架
     */
    private Integer status;
    /**
     * 库存
     */
    private Long stock;
    /**
     * 销售量
     */
    private Long sales;
    /**
     * 冻结库存
     */
    private Long frezzStock;
    /**
     * 当前售价
     */
    private BigDecimal price;
    /**
     * 预警数量
     */
    private Long warningNum;

    /**
     * 是否删除
     */
    private Integer isDelete;

}
