package com.kxmall.web.controller.product.service;

import com.kxmall.product.domain.bo.KxStoreProductAttrResultBo;
import com.kxmall.product.domain.vo.KxStoreProductAttrResultVo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商品属性详情Service接口
 *
 * @author kxmall
 * @date 2023-02-13
 */
public interface IKxStoreProductAttrResultService {

    /**
     * 查询商品属性详情
     */
    KxStoreProductAttrResultVo queryById(Long id);

    /**
     * 查询商品属性详情列表
     */
    TableDataInfo<KxStoreProductAttrResultVo> queryPageList(KxStoreProductAttrResultBo bo, PageQuery pageQuery);

    /**
     * 查询商品属性详情列表
     */
    List<KxStoreProductAttrResultVo> queryList(KxStoreProductAttrResultBo bo);

    /**
     * 新增商品属性详情
     */
    Boolean insertByBo(KxStoreProductAttrResultBo bo);

    /**
     * 修改商品属性详情
     */
    Boolean updateByBo(KxStoreProductAttrResultBo bo);

    /**
     * 校验并批量删除商品属性详情信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
