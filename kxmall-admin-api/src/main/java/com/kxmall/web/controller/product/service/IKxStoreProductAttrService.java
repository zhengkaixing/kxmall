package com.kxmall.web.controller.product.service;

import com.kxmall.product.domain.KxStoreProductAttr;
import com.kxmall.product.domain.bo.KxStoreProductAttrBo;
import com.kxmall.product.domain.vo.FromatDetailVo;
import com.kxmall.product.domain.vo.ProductFormatVo;
import com.kxmall.product.domain.vo.KxStoreProductAttrVo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商品属性Service接口
 *
 * @author kxmall
 * @date 2023-02-13
 */
public interface IKxStoreProductAttrService {

    /**
     * 查询商品属性
     */
    KxStoreProductAttrVo queryById(Long id);

    /**
     * 查询商品属性列表
     */
    TableDataInfo<KxStoreProductAttrVo> queryPageList(KxStoreProductAttrBo bo, PageQuery pageQuery);

    /**
     * 查询商品属性列表
     */
    List<KxStoreProductAttrVo> queryList(KxStoreProductAttrBo bo);

    /**
     * 新增商品属性
     */
    Boolean insertByBo(KxStoreProductAttrBo bo);

    /**
     * 修改商品属性
     */
    Boolean updateByBo(KxStoreProductAttrBo bo);

    /**
     * 校验并批量删除商品属性信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    void insertYxStoreProductAttr(List<FromatDetailVo> toList, List<ProductFormatVo> toList1, Long id);

    List<KxStoreProductAttr> queryListAll();
}
