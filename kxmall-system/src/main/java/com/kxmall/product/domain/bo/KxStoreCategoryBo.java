package com.kxmall.product.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商品分类业务对象 kx_store_category
 *
 * @author kxmall
 * @date 2023-02-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxStoreCategoryBo extends BaseEntity {

    /**
     * 商品分类表ID
     */
    private Long id;

    /**
     * 父id
     */
    private Long pid;

    /**
     * 分类名称
     */
    private String cateName;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 图标
     */
    private String pic;

    /**
     * 是否推荐
     */
    private Integer isShow;

    /**
     * 删除状态
     */
    private Integer isDel;


}
