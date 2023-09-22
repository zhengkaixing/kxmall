package com.kxmall.web.controller.storage.service;

import com.kxmall.storage.domain.vo.KxGoodsInStockVo;
import com.kxmall.storage.domain.bo.KxGoodsInStockBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.storage.domain.vo.KxStorageVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品入库Service接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface IKxGoodsInStockService {

    /**
     * 查询商品入库
     */
    KxGoodsInStockVo queryById(Long id);

    /**
     * 查询商品入库列表
     */
    TableDataInfo<KxGoodsInStockVo> queryPageList(KxGoodsInStockBo bo, PageQuery pageQuery);

    /**
     * 查询商品入库列表
     */
    List<KxGoodsInStockVo> queryList(KxGoodsInStockBo bo);

    /**
     * 新增商品入库
     */
    Boolean insertByBo(KxGoodsInStockBo bo);

    /**
     * 修改商品入库
     */
    Boolean updateByBo(KxGoodsInStockBo bo);

    /**
     * 校验并批量删除商品入库信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 入库
     * @param bo
     * @return
     */
    Boolean updateInOfStock(KxGoodsInStockBo bo);

    /**
     * 获取所有仓库的名称
     * @param bo
     * @return
     */
    List<KxStorageVo> storagAllName(KxGoodsInStockBo bo);
}
