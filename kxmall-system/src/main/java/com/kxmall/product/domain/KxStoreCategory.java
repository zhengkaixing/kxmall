package com.kxmall.product.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商品分类对象 kx_store_category
 *
 * @author kxmall
 * @date 2023-02-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_category")
public class KxStoreCategory extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 商品分类表ID
     */
    @TableId(value = "id")
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
