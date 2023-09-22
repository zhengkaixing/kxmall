
package com.kxmall.product.domain.vo;

import lombok.*;

import java.util.Map;



@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductFormatVo {

    private String sku = "";

    private String barCode = "";

    private Double brokerage = 0d;

    private Double brokerageTwo = 0d;

    private Double price = 0d;

    private Double otPrice = 0d;

    private Double cost = 0d;

    private Long stock = 0L;

    private Long integral = 0L;

    private String pic = "";

    private String value1 = "";

    private String value2 = "";

    private Double volume = 0d;

    private Double weight = 0d;
    private Double pinkPrice = 0d;
    private Integer pinkStock = 0;
    private Double seckillPrice = 0d;
    private Integer seckillStock = 0;

    private Map<String, String> detail;

}
