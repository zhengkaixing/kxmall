package com.kxmall.web.controller.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.OrderStatusType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.order.domain.KxStoreAppraise;
import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.domain.KxStoreOrderProduct;
import com.kxmall.order.domain.bo.AppraiseRequestBo;
import com.kxmall.order.domain.bo.KxStoreAppraiseBo;
import com.kxmall.order.domain.vo.KxStoreAppraiseVo;
import com.kxmall.order.mapper.KxStoreAppraiseMapper;
import com.kxmall.order.mapper.KxStoreOrderMapper;
import com.kxmall.order.mapper.KxStoreOrderProductMapper;
import com.kxmall.web.controller.order.service.IKxAppAppraiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/6
 */

@RequiredArgsConstructor
@Service
public class KxAppAppraiseService implements IKxAppAppraiseService {


    private final KxStoreOrderMapper orderMapper;

    private final KxStoreOrderProductMapper orderProductMapper;

    private final KxStoreAppraiseMapper baseMapper;

    public static final String CA_APPRAISE_KEY = "CA_APPRAISE_";

    @Override
    public TableDataInfo<KxStoreAppraiseVo> getProductAppraiseByPage(Long productId, Integer pageNo, Integer pageSize, Integer state) {
        String cacheKey = CA_APPRAISE_KEY + productId + "_" + pageNo + "_" + pageSize;
        TableDataInfo<KxStoreAppraiseVo> obj = RedisUtils.getCacheObject(cacheKey);
        if (obj != null) {
            return obj;
        }
        Long count = baseMapper.selectCount(new QueryWrapper<KxStoreAppraise>().eq("product_id", productId).eq("state", state));
        Integer offset = pageSize * (pageNo - 1);
        List<KxStoreAppraiseVo> storeAppraiseVoList = baseMapper.selectProductAppraiseByPage(productId, offset, pageSize);
//        for (KxStoreAppraiseVo appraiseResponseDTO : storeAppraiseVoList) {
//            appraiseResponseDTO.setImgList(imgMapper.getImgs(BizType.COMMENT.getCode(), appraiseResponseDTO.getId()));
//        }

        TableDataInfo<KxStoreAppraiseVo> info = new TableDataInfo<>(storeAppraiseVoList, count);
        RedisUtils.setCacheObject(cacheKey, info);
        return info;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addAppraise(AppraiseRequestBo bo, Long userId) throws ServiceException {
        if (bo.getOrderId() == null) {
            throw new ServiceException("参数校验失败");
        }
        //校验是否有对应等待评价的订单
        Long integer = orderMapper.selectCount(
                new QueryWrapper<KxStoreOrder>()
                        .eq("id", bo.getOrderId())
                        .eq("status", OrderStatusType.WAIT_APPRAISE.getCode())
                        .eq("uid", userId));
        if (integer == 0L) {
            throw new ServiceException("当前状态不允许评价");
        }

        //如果传入评价list中没有数据，就直接转变订单状态发出
        Date now = new Date();
        if (CollectionUtils.isEmpty(bo.getAppraiseDTOList())) {
            KxStoreOrder orderDO = KxStoreOrder.builder().build();
            orderDO.setStatus(OrderStatusType.COMPLETE.getCode());
            orderDO.setId(bo.getOrderId());
            orderDO.setUpdateTime(now);
            orderMapper.updateById(orderDO);
        }

        //循环读取订单评价中所有商品的评价
        for (KxStoreAppraiseBo appraiseDTO : bo.getAppraiseDTOList()) {
            Long count = orderProductMapper.selectCount(new QueryWrapper<KxStoreOrderProduct>()
                    .eq("order_id", bo.getOrderId())
                    .eq("product_id", appraiseDTO.getProductId())
                    .eq("product_attr_id", appraiseDTO.getProductAttrId()));
            //从order_sku表中 验证是否有对应的表单和商品
            if (count == 0L) {
                throw new ServiceException("当前状态不允许评价");
            }

            KxStoreAppraise appraiseDO = new KxStoreAppraise();
            BeanUtils.copyProperties(appraiseDTO, appraiseDO);
            appraiseDO.setProductId(appraiseDTO.getProductId());
            appraiseDO.setId(null); //防止传入id,导致插入数据库出错
            appraiseDO.setOrderId(bo.getOrderId()); //从传入数据取出，不使用DTO中的冗余数据
            appraiseDO.setUserId(userId);
            appraiseDO.setUpdateTime(now);
            appraiseDO.setCreateTime(now);
            appraiseDO.setContent(appraiseDTO.getContent());
            baseMapper.insert(appraiseDO);  //插入该订单该商品评价
            RedisUtils.deleteKeys(CA_APPRAISE_KEY + appraiseDO.getProductId()+"*"); //删除商品评论缓存

        }
        //改变订单状态
        KxStoreOrder orderDO = KxStoreOrder.builder().build();
        orderDO.setStatus(OrderStatusType.COMPLETE.getCode());
        orderDO.setId(bo.getOrderId());
        orderDO.setUpdateTime(now);
        orderMapper.updateById(orderDO);
        return true;
    }


}
