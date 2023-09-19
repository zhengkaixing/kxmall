package com.kxmall.web.controller.storage.service;

import com.kxmall.storage.domain.vo.KxGoodsOutStockVo;
import com.kxmall.storage.domain.bo.KxGoodsOutStockBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.storage.domain.vo.KxStorageVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品出库Service接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface IKxGoodsOutStockService {

    /**
     * 查询商品出库
     */
    KxGoodsOutStockVo queryById(Long id);

    /**
     * 查询商品出库列表
     */
    TableDataInfo<KxGoodsOutStockVo> queryPageList(KxGoodsOutStockBo bo, PageQuery pageQuery);

    /**
     * 查询商品出库列表
     */
    List<KxGoodsOutStockVo> queryList(KxGoodsOutStockBo bo);

    /**
     * 新增商品出库
     */
    Boolean insertByBo(KxGoodsOutStockBo bo);

    /**
     * 修改商品出库
     */
    Boolean updateByBo(KxGoodsOutStockBo bo);

    /**
     * 校验并批量删除商品出库信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 出库
     * @param bo
     * @return
     */
    Boolean updateOutOfStock(KxGoodsOutStockBo bo);

    /**
     * 获取所有仓库的名称
     * @param bo
     * @return
     */
    List<KxStorageVo> storagAllName(KxGoodsOutStockBo bo);
}
