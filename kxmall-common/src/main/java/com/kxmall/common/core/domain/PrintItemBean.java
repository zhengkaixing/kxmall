package com.kxmall.common.core.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/8/5
 */
@Data
@Builder
public class PrintItemBean {

    private String name;
    private BigDecimal price;
    private Integer num;


}
