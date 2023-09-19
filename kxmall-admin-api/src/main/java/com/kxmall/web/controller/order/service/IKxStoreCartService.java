package com.kxmall.web.controller.order.service;

import com.kxmall.order.domain.vo.KxStoreCartVo;
import com.kxmall.order.domain.bo.KxStoreCartBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 购物车Service接口
 *
 * @author kxmall
 * @date 2023-02-15
 */
public interface IKxStoreCartService {

    /**
     * 查询购物车
     */
    KxStoreCartVo queryById(Long id);

    /**
     * 查询购物车列表
     */
    TableDataInfo<KxStoreCartVo> queryPageList(KxStoreCartBo bo, PageQuery pageQuery);

    /**
     * 查询购物车列表
     */
    List<KxStoreCartVo> queryList(KxStoreCartBo bo);

    /**
     * 新增购物车
     */
    Boolean insertByBo(KxStoreCartBo bo);

    /**
     * 修改购物车
     */
    Boolean updateByBo(KxStoreCartBo bo);

    /**
     * 校验并批量删除购物车信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
