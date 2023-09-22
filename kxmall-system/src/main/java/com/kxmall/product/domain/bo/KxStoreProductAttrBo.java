package com.kxmall.product.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商品属性业务对象 kx_store_product_attr
 *
 * @author kxmall
 * @date 2023-02-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxStoreProductAttrBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 属性名
     */
    private String attrName;

    /**
     * 属性值
     */
    private String attrValues;

    /**
     * 删除状态
     */
    private Integer isDel;


}
