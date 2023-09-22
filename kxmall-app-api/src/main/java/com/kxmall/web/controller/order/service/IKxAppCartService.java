package com.kxmall.web.controller.order.service;

import com.kxmall.order.domain.vo.KxStoreCartVo;

import java.util.List;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/3
 */
public interface IKxAppCartService {

    /**
     * 获取购物车数量
     */
    Long countCart(Long storageId, Long userId);

    /**
     * 获取购物车商品列表
     */
    List<KxStoreCartVo> getCartList(Long storageId, Long userId);

    /**
     * 获取购物车商品列表
     * @param productId
     * @param num
     * @param userId
     * @return
     */
    KxStoreCartVo addCartItem(Long productId, Long num, Long userId);

    /**
     * 更新购物车数量
     */
    Long updateCartItemNum(Long cartId, Long num, Long userId);
    /**
     * 将购物车商品删除
     */
    Boolean removeCartItem(Long cartId, Long userId);
}
