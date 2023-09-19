package com.kxmall.web.controller.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.BeanCopyUtils;
import com.kxmall.order.domain.KxStoreCart;
import com.kxmall.order.domain.vo.KxStoreCartVo;
import com.kxmall.order.mapper.KxStoreCartMapper;
import com.kxmall.system.domain.vo.SysOssVo;
import com.kxmall.web.controller.order.service.IKxAppCartService;
import com.kxmall.web.controller.product.service.IKxAppCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/6
 */
@RequiredArgsConstructor
@Service
public class KxAppCartService implements IKxAppCartService {

    private final KxStoreCartMapper baseMapper;

    private final IKxAppCategoryService appCategoryService;

    @Override
    public Long countCart(Long storageId, Long userId) {
        Long userCountCart = baseMapper.countCart(userId,storageId);
        return (userCountCart == null) ? 0L : userCountCart;
    }

    @Override
    public List<KxStoreCartVo> getCartList(Long storageId, Long userId) {
        List<KxStoreCartVo> cartList = baseMapper.getCartList(userId, storageId);
        for (KxStoreCartVo cartVo : cartList) {
            cartVo.setCateIdList(appCategoryService.getCategoryFamily(cartVo.getCateId()));
        }
        return cartList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KxStoreCartVo addCartItem(Long productId, Long num, Long userId) {
        List<KxStoreCart> storeCartList = baseMapper.selectList(
            new QueryWrapper<KxStoreCart>()
                .eq("product_id", productId)
                .eq("uid", userId));
        KxStoreCart storeCart = new KxStoreCart();
        Date now = new Date();
        if (!CollectionUtils.isEmpty(storeCartList)) {
            //若非空
            storeCart.setId(storeCartList.get(0).getId());
            storeCart.setCartNum(storeCartList.get(0).getCartNum() + num);
            storeCart.setUpdateTime(now);
            if (baseMapper.updateById(storeCart) <= 0) {
                throw new ServiceException("购物车更新失败!");
            }
        } else {
            //不存在，则添加购物车
            storeCart.setProductId(productId);
            storeCart.setCartNum(num);
            storeCart.setUid(userId);
            storeCart.setUpdateTime(now);
            storeCart.setCreateTime(now);
            if (baseMapper.insert(storeCart) <= 0) {
                throw new ServiceException("购物车更新失败!");
            }
        }
        KxStoreCartVo cartVo = new KxStoreCartVo();
        BeanCopyUtils.copy(storeCart, cartVo);
        return cartVo;
    }

    @Override
    public Long updateCartItemNum(Long cartId, Long num, Long userId) {
        KxStoreCart cart = new KxStoreCart();
        cart.setCartNum(num);
        int update = baseMapper.update(cart,
            new QueryWrapper<KxStoreCart>()
                .eq("id", cartId)
                .eq("uid", userId));
        if (update > 0) {
            return num;
        }
        throw new ServiceException("购物车更新失败!");
    }

    @Override
    public Boolean removeCartItem(Long cartId, Long userId) {
        return baseMapper.delete(
            new QueryWrapper<KxStoreCart>()
                .eq("id", cartId)
                .eq("uid", userId)) > 0;
    }
}
