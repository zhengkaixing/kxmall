package com.kxmall.web.controller.storage.service;

import com.kxmall.storage.domain.vo.KxInStockProductVo;
import com.kxmall.storage.domain.bo.KxInStockProductBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 入库商品Service接口
 *
 * @author kxmall
 * @date 2023-08-29
 */
public interface IKxInStockProductService {

    /**
     * 查询入库商品
     */
    KxInStockProductVo queryById(Long id);

    /**
     * 查询入库商品列表
     */
    TableDataInfo<KxInStockProductVo> queryPageList(KxInStockProductBo bo, PageQuery pageQuery);

    /**
     * 查询入库商品列表
     */
    List<KxInStockProductVo> queryList(KxInStockProductBo bo);

    /**
     * 新增入库商品
     */
    Boolean insertByBo(KxInStockProductBo bo);

    /**
     * 修改入库商品
     */
    Boolean updateByBo(KxInStockProductBo bo);

    /**
     * 校验并批量删除入库商品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
