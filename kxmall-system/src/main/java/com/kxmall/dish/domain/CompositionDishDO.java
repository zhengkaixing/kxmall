package com.kxmall.dish.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Generate Code By kxmall
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("kx_composition_dish")
public class CompositionDishDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券表ID
     */
    @TableId(value = "id")
    private Long id;

    private Long storageId;

    private String title;

    private Integer sales;

    private BigDecimal score;

    private String img;
    private String detail;
    private String description;
    private Long categoryId;
    private Integer status;

}
