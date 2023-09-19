package com.kxmall.order.domain.bo;

import lombok.Data;

/**
 * Created by admin on 2019/7/6.
 */
@Data
public class OrderRequestProductBo {

    private Long productId;

    private Integer price;

    private Integer cartNum;

    private String productImg;

    private String productAttrImg;





}
