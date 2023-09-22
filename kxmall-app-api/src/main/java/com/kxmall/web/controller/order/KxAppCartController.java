package com.kxmall.web.controller.order;

import cn.dev33.satoken.annotation.SaIgnore;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.R;
import com.kxmall.order.domain.vo.KxStoreCartVo;
import com.kxmall.storage.domain.vo.RecentlyStorageVo;
import com.kxmall.web.controller.order.service.IKxAppCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * app购物车
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart/app")
public class KxAppCartController extends BaseAppController {


    private final IKxAppCartService kxAppCartService;


    /**
     * 获取购物车数量
     */
    @GetMapping("/countCart")
    public R<Long> countCart(Long storageId) {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(kxAppCartService.countCart(storageId,userId));
    }

    /**
     * 获取购物车商品列表
     */
    @GetMapping("/getCartList")
    public R<List<KxStoreCartVo>> getCartList(Long storageId) {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(kxAppCartService.getCartList(storageId,userId));
    }


    /**
     * 获取购物车商品列表
     */
    @GetMapping("/addCartItem")
    public R<KxStoreCartVo> addCartItem(Long productId,Long num) {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(kxAppCartService.addCartItem(productId,num,userId));
    }


    /**
     * 更新购物车数量
     */
    @GetMapping("/updateCartItemNum")
    public R<Long> updateCartItemNum(Long cartId,Long num) {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(kxAppCartService.updateCartItemNum(cartId,num,userId));
    }

    /**
     * 将购物车商品删除
     */
    @GetMapping("/removeCartItem")
    public R<Boolean> removeCartItem(Long cartId) {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(kxAppCartService.removeCartItem(cartId,userId));
    }
}
