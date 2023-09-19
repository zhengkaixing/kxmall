package com.kxmall.storage.mapper;

import com.kxmall.storage.domain.KxGoodsInStock;
import com.kxmall.storage.domain.bo.KxGoodsInStockBo;
import com.kxmall.storage.domain.vo.KxGoodsInStockVo;
import com.kxmall.common.core.mapper.BaseMapperPlus;

/**
 * 商品入库Mapper接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface KxGoodsInStockMapper extends BaseMapperPlus<KxGoodsInStockMapper, KxGoodsInStock, KxGoodsInStockVo> {

    KxGoodsInStock selectByMax();
}
