package com.kxmall.order.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 售后子对象 kx_store_after_sales_item
 *
 * @author kxmall
 * @date 2024-11-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_after_sales_item")
public class KxStoreAfterSalesItem extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 售后id
     */
    private Long storeAfterSalesId;
    /**
     * 商品id
     */
    private Long productId;
    /**
     * 退货东西的详情信息
     */
    private String cartInfo;
    /**
     * 删除状态
     */
    private Integer isDel;

}
