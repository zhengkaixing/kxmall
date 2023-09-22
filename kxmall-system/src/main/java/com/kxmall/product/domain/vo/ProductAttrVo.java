package com.kxmall.product.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductAttrVo implements Serializable {

    private Long productId;

    private String sku;

    private Double price;

    private String image;
}
