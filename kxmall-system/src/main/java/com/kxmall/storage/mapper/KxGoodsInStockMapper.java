package com.kxmall.storage.mapper;

import com.kxmall.common.core.mapper.BaseMapperPlus;
import com.kxmall.storage.domain.KxGoodsInStock;
import com.kxmall.storage.domain.vo.KxGoodsInStockVo;

/**
 * 商品入库Mapper接口
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-08-27
 */
public interface KxGoodsInStockMapper extends BaseMapperPlus<KxGoodsInStockMapper, KxGoodsInStock, KxGoodsInStockVo> {

    KxGoodsInStock selectByMax();
}
