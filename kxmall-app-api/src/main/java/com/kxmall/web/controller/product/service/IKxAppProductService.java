package com.kxmall.web.controller.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.product.domain.vo.KxStoreProductVo;

import java.awt.*;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/3
 */
public interface IKxAppProductService {

    /**
     * 获取商品列表
     * @param pageNo
     * @param pageSize
     * @param categoryId
     * @param orderBy
     * @param isAsc
     * @param title
     * @return
     */
    TableDataInfo<KxStoreProductVo> getGoodsPage(Integer pageNo, Integer pageSize, Long categoryId, String orderBy, Boolean isAsc, String title);

    /**
     * 获取仓库商品列表
     * @param storageId
     * @param pageNo
     * @param pageSize
     * @param categoryId
     * @param orderBy
     * @param isAsc
     * @param title
     * @return
     */
    TableDataInfo<KxStoreProductVo> getGoodsPageByStorage(Long storageId, Integer pageNo, Integer pageSize, Long categoryId, String orderBy, Boolean isAsc, String title);

    /**
     * 指定仓库下获取商品详情
     * @param storageId
     * @param userId
     * @return
     */
    KxStoreProductVo getGoodsByStorage(Long storageId,Long productId, Long userId);
}
