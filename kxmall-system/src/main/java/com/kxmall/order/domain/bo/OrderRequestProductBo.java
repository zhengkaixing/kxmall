package com.kxmall.order.domain.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by admin on 2019/7/6.
 */
@Data
public class OrderRequestProductBo {

    private Long productId;

    private BigDecimal price;

    private Integer cartNum;

    private String productImg;

    private String productAttrImg;





}
