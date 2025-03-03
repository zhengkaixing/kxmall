package com.kxmall.group.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * 团购商品业务对象 kx_group_shop_product
 *
 * @author kxmall
 * @date 2023-10-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxGroupShopProductBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 团购商品
     */
    private Long productAttrId;

    /**
     * 团购主键
     */
    private Long groupShopId;

    /**
     * 团购价格
     */
    private BigDecimal productGroupShopPrice;


}
