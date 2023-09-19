package com.kxmall.web.controller.storage;

import cn.dev33.satoken.annotation.SaIgnore;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.product.domain.vo.KxStoreProductVo;
import com.kxmall.recommend.domain.vo.KxRecommendVo;
import com.kxmall.storage.domain.vo.IntegralIndexDataVo;
import com.kxmall.storage.domain.vo.KxStorageVo;
import com.kxmall.storage.domain.vo.RecentlyStorageVo;
import com.kxmall.web.controller.product.service.IKxAppProductService;
import com.kxmall.web.controller.storage.service.IKxAppStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 仓库管理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage/position")
public class KxAppStorageController extends BaseAppController {

    private final IKxAppStorageService iKxAppStorageService;

    private final IKxAppProductService appProductService;


    /**
     * 获取仓库信息
     */
    @GetMapping("/getStorage")
    public R<KxStorageVo> getStorage(Long storageId) {
        return R.ok(iKxAppStorageService.getStorage(storageId));
    }

    /**
     * 获取最近的仓库
     */
    @SaIgnore
    @GetMapping("/getRecentlyStorage")
    public R<RecentlyStorageVo> getRecentlyStorage(BigDecimal longitude, BigDecimal latitude) {
        return R.ok(iKxAppStorageService.getRecentlyStorage(longitude, latitude));
    }


    /**
     * 获取最近的仓库推荐内容
     */
    @SaIgnore
    @GetMapping("/getRecommendByStorage")
    public R<TableDataInfo<KxRecommendVo>> getRecommendByStorage(Long storageId,
                                                                 Integer recommendType,
                                                                 @RequestParam(defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok(iKxAppStorageService.getRecommendByStorage(storageId, recommendType,pageNo,pageSize));
    }

    /**
     * 获取最近的仓库分页商品
     */
    @SaIgnore
    @GetMapping("/getGoodsPageByStorage")
    public R<TableDataInfo<KxStoreProductVo>> getGoodsPageByStorage(Long storageId,
                                                                    @RequestParam(defaultValue = "1") Integer pageNo,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    Long categoryId,
                                                                    String orderBy,
                                                                    Boolean isAsc,
                                                                    String title) {
        TableDataInfo<KxStoreProductVo> storage = appProductService.getGoodsPageByStorage(storageId, pageNo, pageSize, categoryId, orderBy, isAsc, title);
        return R.ok(storage);
    }


    /**
     * 获取指定仓库数据内容
     */
    @SaIgnore
    @GetMapping("/getIndexDataByStorage")
    public R<IntegralIndexDataVo> getIndexDataByStorage(Long storageId) {
        return R.ok(iKxAppStorageService.getIndexDataByStorage(storageId));
    }


}
