package com.kxmall.web.controller.storage.service;

import com.kxmall.storage.domain.bo.WarningStockBo;
import com.kxmall.storage.domain.vo.KxStockVo;
import com.kxmall.storage.domain.bo.KxStockBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 前置仓商品Service接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface IKxStockService {

    /**
     * 查询前置仓商品
     */
    KxStockVo queryById(Long id);

    /**
     * 查询前置仓商品列表
     */
    TableDataInfo<KxStockVo> queryPageList(KxStockBo bo, PageQuery pageQuery);

    /**
     * 查询前置仓商品列表
     */
    List<KxStockVo> queryList(KxStockBo bo);

    /**
     * 新增前置仓商品
     */
    Boolean insertByBo(KxStockBo bo);

    /**
     * 修改前置仓商品
     */
    Boolean updateByBo(KxStockBo bo);

    /**
     * 校验并批量删除前置仓商品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 上下架
     * @param bo
     * @return
     */
    Boolean freezeOrActivation(KxStockBo bo);


    /**
     * 逻辑删除
     */
    Boolean updateByStock(KxStockBo bo);

    /**
     * 更新当前价格
     * @param bo
     * @return
     */
    Boolean updatePrice(KxStockBo bo);

    /**
     * 查询告警分页
     * @param bo
     * @param pageQuery
     * @return
     */
    TableDataInfo<KxStockVo> queryPageWarningList(WarningStockBo bo, PageQuery pageQuery);

    /**
     * 告警数量更新
     * @param bo
     * @return
     */
    Boolean warningUpdate(WarningStockBo bo);
}
