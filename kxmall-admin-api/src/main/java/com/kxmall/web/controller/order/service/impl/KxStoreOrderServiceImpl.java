package com.kxmall.web.controller.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.OrderLogEnum;
import com.kxmall.common.enums.OrderStatusType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.OrderUtil;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.domain.KxStoreOrderProduct;
import com.kxmall.order.domain.bo.KxStoreOrderBo;
import com.kxmall.order.domain.vo.KxStoreOrderVo;
import com.kxmall.order.mapper.KxStoreOrderMapper;
import com.kxmall.order.mapper.KxStoreOrderProductMapper;
import com.kxmall.user.domain.vo.KxUserVo;
import com.kxmall.web.controller.order.service.IKxStoreOrderService;
import com.kxmall.web.controller.user.service.IKxUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-15
 */
@RequiredArgsConstructor
@Service
public class KxStoreOrderServiceImpl implements IKxStoreOrderService {

    private static final Logger logger = LoggerFactory.getLogger(KxStoreOrderServiceImpl.class);

    private final KxStoreOrderMapper baseMapper;

    private final KxStoreOrderProductMapper orderProductMapper;

    private final IKxUserService kxUserService;

    /**
     * 查询订单
     */
    @Override
    public KxStoreOrderVo queryById(Long id) {
        KxStoreOrderVo kxStoreOrderVo = baseMapper.selectVoById(id);
        if (ObjectUtil.isEmpty(kxStoreOrderVo)) {
            throw new ServiceException("订单详情不存在");
        }

        String payTypeName = OrderUtil.payTypeName("weixin"
                , 1);
        kxStoreOrderVo.setPayTypeName(payTypeName);

        //新增记录产品详情
        kxStoreOrderVo.setProductList(orderProductMapper.selectVoList(new LambdaQueryWrapper<KxStoreOrderProduct>().eq(KxStoreOrderProduct::getOrderId, kxStoreOrderVo.getId())));

        //添加用户信息
        kxStoreOrderVo.setKxUserVo(kxUserService.selectByUid(kxStoreOrderVo.getUid()));
        if (kxStoreOrderVo.getKxUserVo() == null) {
            kxStoreOrderVo.setKxUserVo(new KxUserVo());
        }
        return kxStoreOrderVo;
    }

    /**
     * 查询订单列表
     */
    @Override
    public TableDataInfo<KxStoreOrderVo> queryPageList(KxStoreOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStoreOrder> lqw = buildQueryWrapper(bo);

        List<Integer> status = new ArrayList<>();
        if (StringUtils.isNoneBlank(bo.getOrderStatus())) {
            if (!"all".equals(bo.getOrderStatus())) {
                String[] states = bo.getOrderStatus().split(",");
                for (int i = 0; i < states.length; i++) {
                    status.add(Integer.parseInt(states[i]));
                }
                lqw.in(KxStoreOrder::getStatus, status);
            }
        }
        if (ObjectUtils.isNotEmpty(bo.getStartTime()) && ObjectUtils.isNotEmpty(bo.getEndTime())) {
            lqw.between(KxStoreOrder::getCreateTime, bo.getStartTime(), bo.getEndTime());
        }
        if (ObjectUtils.isNotEmpty(bo.getStorageId())) {
            lqw.eq(KxStoreOrder::getStoreId, bo.getStorageId());
        }
        Page<KxStoreOrderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询订单列表
     */
    @Override
    public List<KxStoreOrderVo> queryList(KxStoreOrderBo bo) {
        LambdaQueryWrapper<KxStoreOrder> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStoreOrder> buildQueryWrapper(KxStoreOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxStoreOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getOrderId()), KxStoreOrder::getOrderId, bo.getOrderId());
        lqw.eq(bo.getUid() != null, KxStoreOrder::getUid, bo.getUid());
        lqw.like(StringUtils.isNotBlank(bo.getRealName()), KxStoreOrder::getRealName, bo.getRealName());
        lqw.likeRight(StringUtils.isNotBlank(bo.getUserPhone()), KxStoreOrder::getUserPhone, bo.getUserPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getUserAddress()), KxStoreOrder::getUserAddress, bo.getUserAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getCartId()), KxStoreOrder::getCartId, bo.getCartId());
        lqw.eq(bo.getFreightPrice() != null, KxStoreOrder::getFreightPrice, bo.getFreightPrice());
        lqw.eq(bo.getTotalNum() != null, KxStoreOrder::getTotalNum, bo.getTotalNum());
        lqw.eq(bo.getTotalPrice() != null, KxStoreOrder::getTotalPrice, bo.getTotalPrice());
        lqw.eq(bo.getTotalPostage() != null, KxStoreOrder::getTotalPostage, bo.getTotalPostage());
        lqw.eq(bo.getPayPrice() != null, KxStoreOrder::getPayPrice, bo.getPayPrice());
        lqw.eq(bo.getPayPostage() != null, KxStoreOrder::getPayPostage, bo.getPayPostage());
        lqw.eq(bo.getDeductionPrice() != null, KxStoreOrder::getDeductionPrice, bo.getDeductionPrice());
        lqw.eq(bo.getCouponId() != null, KxStoreOrder::getCouponId, bo.getCouponId());
        lqw.eq(bo.getCouponPrice() != null, KxStoreOrder::getCouponPrice, bo.getCouponPrice());
        lqw.eq(bo.getPayTime() != null, KxStoreOrder::getPayTime, bo.getPayTime());
        lqw.eq(bo.getStatus() != null, KxStoreOrder::getStatus, bo.getStatus());
        lqw.eq(bo.getRefundStatus() != null, KxStoreOrder::getRefundStatus, bo.getRefundStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRefundReasonWapImg()), KxStoreOrder::getRefundReasonWapImg, bo.getRefundReasonWapImg());
        lqw.eq(StringUtils.isNotBlank(bo.getRefundReasonWapExplain()), KxStoreOrder::getRefundReasonWapExplain, bo.getRefundReasonWapExplain());
        lqw.eq(bo.getRefundReasonTime() != null, KxStoreOrder::getRefundReasonTime, bo.getRefundReasonTime());
        lqw.eq(StringUtils.isNotBlank(bo.getRefundReasonWap()), KxStoreOrder::getRefundReasonWap, bo.getRefundReasonWap());
        lqw.eq(StringUtils.isNotBlank(bo.getRefundReason()), KxStoreOrder::getRefundReason, bo.getRefundReason());
        lqw.eq(bo.getRefundPrice() != null, KxStoreOrder::getRefundPrice, bo.getRefundPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliverySn()), KxStoreOrder::getDeliverySn, bo.getDeliverySn());
        lqw.like(StringUtils.isNotBlank(bo.getDeliveryName()), KxStoreOrder::getDeliveryName, bo.getDeliveryName());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliveryType()), KxStoreOrder::getDeliveryType, bo.getDeliveryType());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliveryId()), KxStoreOrder::getDeliveryId, bo.getDeliveryId());
        lqw.eq(bo.getGainIntegral() != null, KxStoreOrder::getGainIntegral, bo.getGainIntegral());
        lqw.eq(bo.getUseIntegral() != null, KxStoreOrder::getUseIntegral, bo.getUseIntegral());
        lqw.eq(bo.getPayIntegral() != null, KxStoreOrder::getPayIntegral, bo.getPayIntegral());
        lqw.eq(bo.getBackIntegral() != null, KxStoreOrder::getBackIntegral, bo.getBackIntegral());
        lqw.eq(StringUtils.isNotBlank(bo.getMark()), KxStoreOrder::getMark, bo.getMark());
        lqw.eq(bo.getIsDel() != null, KxStoreOrder::getIsDel, bo.getIsDel());
        lqw.eq(bo.getMerId() != null, KxStoreOrder::getMerId, bo.getMerId());
        lqw.eq(bo.getIsMerCheck() != null, KxStoreOrder::getIsMerCheck, bo.getIsMerCheck());
        lqw.eq(bo.getCombinationId() != null, KxStoreOrder::getCombinationId, bo.getCombinationId());
        lqw.eq(bo.getPinkId() != null, KxStoreOrder::getPinkId, bo.getPinkId());
        lqw.eq(bo.getCost() != null, KxStoreOrder::getCost, bo.getCost());
        lqw.eq(bo.getSeckillId() != null, KxStoreOrder::getSeckillId, bo.getSeckillId());
        lqw.eq(bo.getBargainId() != null, KxStoreOrder::getBargainId, bo.getBargainId());
        lqw.eq(StringUtils.isNotBlank(bo.getVerifyCode()), KxStoreOrder::getVerifyCode, bo.getVerifyCode());
        lqw.eq(bo.getStoreId() != null, KxStoreOrder::getStoreId, bo.getStoreId());
        lqw.eq(bo.getShippingType() != null, KxStoreOrder::getShippingType, bo.getShippingType());
        lqw.eq(bo.getIsChannel() != null, KxStoreOrder::getIsChannel, bo.getIsChannel());
        lqw.eq(bo.getIsRemind() != null, KxStoreOrder::getIsRemind, bo.getIsRemind());
        lqw.eq(bo.getIsSystemDel() != null, KxStoreOrder::getIsSystemDel, bo.getIsSystemDel());


        return lqw;
    }


    /**
     * 新增订单
     */
    @Override
    public Boolean insertByBo(KxStoreOrderBo bo) {
        KxStoreOrder add = BeanUtil.toBean(bo, KxStoreOrder.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改订单
     */
    @Override
    public Boolean updateByBo(KxStoreOrderBo bo) {
        KxStoreOrderVo kxStoreOrderVo = baseMapper.selectVoById(bo.getId());
        if (ObjectUtil.isNull(kxStoreOrderVo)) {
            throw new ServiceException("订单不存在");
        }
        KxStoreOrder update = BeanUtil.toBean(bo, KxStoreOrder.class);
        baseMapper.updateById(update);
        return true;

    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStoreOrder entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除订单
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    @Override
    public Boolean startStocking(Long id) {
        return this.updateOrderStatus(id, OrderStatusType.PREPARING_GOODS.getCode(), OrderStatusType.WAIT_PREPARE_GOODS.getCode());
    }

    @Override
    public Boolean completeAllocation(Long id) {
        return this.updateOrderStatus(id, OrderStatusType.WAIT_STOCK.getCode(), OrderStatusType.PREPARING_GOODS.getCode());
    }

    @Override
    public Boolean merchantDistribution(Long id) {
        return this.updateOrderStatus(id, OrderStatusType.WAIT_CONFIRM.getCode(), OrderStatusType.WAIT_STOCK.getCode());
    }

    @Override
    public Boolean completeDelivery(Long id) {
        return this.updateOrderStatus(id, OrderStatusType.WAIT_APPRAISE.getCode(), OrderStatusType.WAIT_CONFIRM.getCode());
    }

    /**
     * 更新状态
     *
     * @param id
     * @param newStatus
     * @param oldStatus
     * @return
     */
    private Boolean updateOrderStatus(Long id, Integer newStatus, Integer oldStatus) {
        try {
            KxStoreOrder storeOrder = KxStoreOrder.builder().build();
            storeOrder.setStatus(newStatus);
            return baseMapper.update(storeOrder, new QueryWrapper<KxStoreOrder>().eq("id" , id).eq("status" , oldStatus)) > 0;
        } catch (Exception e) {
            logger.error("[订单状态扭转] 异常" , e);
            throw new ServiceException("订单系统未知异常");
        }
    }


}
