package com.kxmall.order.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 售后子业务对象 kx_store_after_sales_item
 *
 * @author kxmall
 * @date 2024-11-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxStoreAfterSalesItemBo extends BaseEntity {

    /**
     * 主键id
     */
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
