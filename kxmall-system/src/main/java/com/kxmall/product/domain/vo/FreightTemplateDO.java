package com.kxmall.product.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Description:
 * User: admin
 * Date: 2019-07-07
 * Time: 上午11:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreightTemplateDO {

    private String templateName;

    //商品发货地址
    private String spuLocation;

    //多久时间内发货，一天还是几天
    private Integer deliveryDeadline;

    //0包邮 -1永不包邮，正数表示满好多包邮
    private Integer defaultFreePrice;

    private Integer defaultFirstNum;

    private BigDecimal defaultFirstMoney;

    private Integer defaultContinueNum;

    private Integer defaultContinueMoney;

}
