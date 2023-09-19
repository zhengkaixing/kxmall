package com.kxmall.order.mapper;

import com.kxmall.order.domain.KxStoreCart;
import com.kxmall.order.domain.vo.KxStoreCartVo;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车Mapper接口
 *
 * @author kxmall
 * @date 2023-02-15
 */
public interface KxStoreCartMapper extends BaseMapperPlus<KxStoreCartMapper, KxStoreCart, KxStoreCartVo> {

    /**
     * 获取购物车数量
     */
    Long countCart(@Param("userId") Long userId, @Param("storageId") Long storageId);

    /**
     * 获取购物车商品列表
     */
    List<KxStoreCartVo> getCartList(@Param("userId") Long userId, @Param("storageId") Long storageId);
}
