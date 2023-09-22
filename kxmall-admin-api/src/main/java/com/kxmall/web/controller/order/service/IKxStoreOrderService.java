package com.kxmall.web.controller.order.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.order.domain.bo.KxStoreOrderBo;
import com.kxmall.order.domain.vo.KxStoreOrderVo;

import java.util.Collection;
import java.util.List;

/**
 * 订单Service接口
 *
 * @author kxmall
 * @date 2023-02-15
 */
public interface IKxStoreOrderService {

    /**
     * 查询订单
     */
    KxStoreOrderVo queryById(Long id);

    /**
     * 查询订单列表
     */
    TableDataInfo<KxStoreOrderVo> queryPageList(KxStoreOrderBo bo, PageQuery pageQuery);

    /**
     * 查询订单列表
     */
    List<KxStoreOrderVo> queryList(KxStoreOrderBo bo);

    /**
     * 新增订单
     */
    Boolean insertByBo(KxStoreOrderBo bo);

    /**
     * 修改订单
     */
    Boolean updateByBo(KxStoreOrderBo bo);

    /**
     * 校验并批量删除订单信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 开始配货
     *
     * @param orderId
     * @return
     */
    Boolean startStocking(Long id);

    /**
     * 完成配货
     *
     * @param id
     * @return
     */
    Boolean completeAllocation(Long id);

    /**
     * 商家自配
     *
     * @param id
     * @return
     */
    Boolean merchantDistribution(Long id);

    /**
     * 完成配送
     *
     * @param id
     * @return
     */
    Boolean completeDelivery(Long id);
}
