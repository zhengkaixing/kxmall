package com.kxmall.order.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 购物车业务对象 kx_store_cart
 *
 * @author kxmall
 * @date 2023-02-15
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxStoreCartBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 订单id
     */
    private Long oid;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 购物车id
     */
    private Long cartId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 购买东西的详细信息
     */
    private String cartInfo;

    /**
     * 唯一id
     */
    private String unique;

    /**
     * 是否能售后0不能1能
     */
    private Integer isAfterSales;


}
