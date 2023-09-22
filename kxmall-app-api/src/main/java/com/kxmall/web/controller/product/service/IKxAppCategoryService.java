package com.kxmall.web.controller.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kxmall.product.domain.KxStoreCategory;
import com.kxmall.product.domain.vo.KxStoreCategoryVo;

import java.util.List;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/3
 */
public interface IKxAppCategoryService {

    /**
     *
     * @param cartId
     * @return
     */
    List<Long> getCategoryFamily(Long cateId);

    /**
     * 查询分类列表
     * @param eq
     * @return
     */
    List<KxStoreCategory> selectList(LambdaQueryWrapper<KxStoreCategory> eq);

    /**
     * 获取分类对象
     * @param categoryId
     * @return
     */
    KxStoreCategory selectById(Long categoryId);

    /**
     * 查询所有目录
     *
     * @return
     */
    List<KxStoreCategoryVo> categoryList();
}
