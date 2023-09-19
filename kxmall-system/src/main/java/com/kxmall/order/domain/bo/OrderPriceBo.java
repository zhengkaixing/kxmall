package com.kxmall.order.domain.bo;

import com.kxmall.product.domain.vo.KxStoreProductVo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @description: 订单创建时价格的传输实体类
 * @author: fy
 * @date: 2020/03/13 13:39
 **/
@Data
public class OrderPriceBo implements Serializable {

    private BigDecimal productOriginalTotalPrice;

    /*** 商品总价*/
    private BigDecimal productTotalPrice;

    /*** 运费*/
    private BigDecimal freightPrice;

    /*** 优惠券*/
    private BigDecimal couponPrice;

    /*** 优惠券ID*/
    private Long couponId;

    /*** 团购*/
    private Long groupShopId;

    /*** 计算优惠后，实际需要支付的价格*/
    private BigDecimal actualPrice;

    /*** 支付加个*/
    private Integer payPrice;


    /***每个地方仓库的价格不一样,各自商户设置的价格对应仓库的价格 */
    Map<Long, KxStoreProductVo> productIdDTOMap;

}
