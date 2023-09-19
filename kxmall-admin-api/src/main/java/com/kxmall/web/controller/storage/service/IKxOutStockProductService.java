package com.kxmall.web.controller.storage.service;

import com.kxmall.storage.domain.vo.KxOutStockProductVo;
import com.kxmall.storage.domain.bo.KxOutStockProductBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 出库商品Service接口
 *
 * @author kxmall
 * @date 2023-08-29
 */
public interface IKxOutStockProductService {

    /**
     * 查询出库商品
     */
    KxOutStockProductVo queryById(Long id);

    /**
     * 查询出库商品列表
     */
    TableDataInfo<KxOutStockProductVo> queryPageList(KxOutStockProductBo bo, PageQuery pageQuery);

    /**
     * 查询出库商品列表
     */
    List<KxOutStockProductVo> queryList(KxOutStockProductBo bo);

    /**
     * 新增出库商品
     */
    Boolean insertByBo(KxOutStockProductBo bo);

    /**
     * 修改出库商品
     */
    Boolean updateByBo(KxOutStockProductBo bo);

    /**
     * 校验并批量删除出库商品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
