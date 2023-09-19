package com.kxmall.product.domain.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class ProductResultVo {
    private Double minPrice;

    private Double minOtPrice;

    private Double minCost;

    private Long stock;

    private Long minIntegral;
}
