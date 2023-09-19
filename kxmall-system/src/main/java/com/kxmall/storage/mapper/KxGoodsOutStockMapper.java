package com.kxmall.storage.mapper;

import com.kxmall.storage.domain.KxGoodsOutStock;
import com.kxmall.storage.domain.vo.KxGoodsOutStockVo;
import com.kxmall.common.core.mapper.BaseMapperPlus;

/**
 * 商品出库Mapper接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface KxGoodsOutStockMapper extends BaseMapperPlus<KxGoodsOutStockMapper, KxGoodsOutStock, KxGoodsOutStockVo> {


    /**
     * 出库商品数更新
     *
     * @return
     */
    KxGoodsOutStock selectByMax();
}
