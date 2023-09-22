package com.kxmall.web.controller.product;

import cn.dev33.satoken.annotation.SaIgnore;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.R;
import com.kxmall.product.domain.vo.KxStoreCategoryVo;
import com.kxmall.product.domain.vo.KxStoreProductVo;
import com.kxmall.web.controller.product.service.IKxAppCategoryService;
import com.kxmall.web.controller.product.service.IKxAppProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * app商品管理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/app")
public class KxAppProductController extends BaseAppController {


    private final IKxAppProductService appProductService;
    private final IKxAppCategoryService appCategoryService;


    /**
     * 指定仓库下获取商品详情
     */
    @SaIgnore
    @GetMapping("/getGoodsByStorage")
    public R<KxStoreProductVo> getGoodsByStorage(Long storageId, Long productId) {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(appProductService.getGoodsByStorage(storageId, productId, userId));
    }


    /**
     * 查询商品分类列表
     */
    @SaIgnore
    @GetMapping("/categoryList")
    public R<List<KxStoreCategoryVo>> categoryList() {
        return R.ok(appCategoryService.categoryList());
    }

}
