package com.kxmall.dish.domain.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Generate Code By kxmall
 */
@Data
public class CompositionDishDTO {

    private Long id;

    private String storageName;

    private String title;

    private Integer sales;

    private BigDecimal score;

    private String detail;

    private Integer status;

    private String img;

    private Long storageId;

}
