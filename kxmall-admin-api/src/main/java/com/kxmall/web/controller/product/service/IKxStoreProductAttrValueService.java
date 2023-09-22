package com.kxmall.web.controller.product.service;

import com.kxmall.product.domain.bo.KxStoreProductAttrValueBo;
import com.kxmall.product.domain.vo.KxStoreProductAttrValueVo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商品属性值Service接口
 *
 * @author kxmall
 * @date 2023-02-13
 */
public interface IKxStoreProductAttrValueService {

    /**
     * 查询商品属性值
     */
    KxStoreProductAttrValueVo queryById(Long id);

    /**
     * 查询商品属性值列表
     */
    TableDataInfo<KxStoreProductAttrValueVo> queryPageList(KxStoreProductAttrValueBo bo, PageQuery pageQuery);

    /**
     * 查询商品属性值列表
     */
    List<KxStoreProductAttrValueVo> queryList(KxStoreProductAttrValueBo bo);

    /**
     * 新增商品属性值
     */
    Boolean insertByBo(KxStoreProductAttrValueBo bo);

    /**
     * 修改商品属性值
     */
    Boolean updateByBo(KxStoreProductAttrValueBo bo);

    /**
     * 校验并批量删除商品属性值信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
