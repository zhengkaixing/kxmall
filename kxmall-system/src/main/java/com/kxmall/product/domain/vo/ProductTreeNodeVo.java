package com.kxmall.product.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 树形
 */
@Data
public class ProductTreeNodeVo {

    private String value;

    private String label;

    private Long id;

    private List<ProductTreeNodeVo> children;

}
