package com.kxmall.web.controller.order.builder;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kxmall.address.domain.KxAddress;
import com.kxmall.address.mapper.KxAddressMapper;
import com.kxmall.common.enums.OrderStatusType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.GeneratorUtil;
import com.kxmall.order.domain.KxStoreCart;
import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.domain.KxStoreOrderProduct;
import com.kxmall.order.domain.bo.OrderPriceBo;
import com.kxmall.order.domain.bo.OrderRequestBo;
import com.kxmall.order.domain.bo.OrderRequestProductBo;
import com.kxmall.order.mapper.KxStoreCartMapper;
import com.kxmall.order.mapper.KxStoreOrderMapper;
import com.kxmall.order.mapper.KxStoreOrderProductMapper;
import com.kxmall.orderobserve.OrderObserveAble;
import com.kxmall.orderobserve.OrderUpdater;
import com.kxmall.product.domain.vo.KxStoreProductVo;
import com.kxmall.product.mapper.KxStoreProductMapper;
import com.kxmall.web.controller.product.service.IKxAppCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 订单具体建造者
 * @author: fy
 * @date: 2020/03/13 13:05
 **/
@Component
@SuppressWarnings("all")
public class OrderConcreteBuilder extends OrderBuilder {

    private static final Logger logger = LoggerFactory.getLogger(OrderConcreteBuilder.class);

    @Autowired
    private KxStoreProductMapper productMapper;

    @Autowired
    private KxStoreCartMapper cartMapper;

    @Autowired
    private KxStoreOrderMapper orderMapper;

    @Autowired
    private KxAddressMapper addressMapper;

    @Resource
    private OrderObserveAble observeAble;

    @Autowired
    private KxStoreOrderProductMapper orderProductMapper;


    @Autowired
    private IKxAppCategoryService categoryService;


    /**
     * 1.订单初始创建校验部分
     *
     * @param orderRequest
     * @param userId
     */
    @Override
    public void buildOrderCheckHandlePart(OrderRequestBo orderRequest, Long userId) throws ServiceException {
        //参数强校验 START
        List<OrderRequestProductBo> skuList = orderRequest.getProductList();
        if (CollectionUtils.isEmpty(skuList) || orderRequest.getTotalPrice() == null) {
            throw new ServiceException("参数校验失败");
        }
        if (orderRequest.getTotalPrice() <= 0) {
            throw new ServiceException("订单金额必须大于0");
        }
    }

    /**
     * 2.订单价格处理部分
     *
     * @param orderDO
     * @param orderRequest
     * @param userId
     */
    @Override
    public OrderPriceBo buildOrderPriceHandlePart(KxStoreOrder orderDO, OrderRequestBo orderRequest, Long userId) throws ServiceException {
        List<OrderRequestProductBo> skuList = orderRequest.getProductList();
        Long groupShopId = orderRequest.getGroupShopId();
        BigDecimal groupShopPrice = null;
        //商品价格
        BigDecimal productPrice = BigDecimal.ZERO;
        BigDecimal productOriginalPrice = BigDecimal.ZERO;
        //稍后用于优惠券作用范围校验
        Map<Long, BigDecimal> categoryPriceMap = new HashMap<>();
        //稍后用于插入KxStoreProductVo
        KxStoreProductVo product;
        Map<Long, KxStoreProductVo> productIdDTOMap = new HashMap<>();
        for (OrderRequestProductBo orderRequestKxStoreProduct : skuList) {
            //每个地方仓库的价格不一样，所以需要查询各自商户设置的价格
            product = productMapper.getProductByIdAndStorageId(orderRequestKxStoreProduct.getProductId(), orderRequest.getStorageId());
            productIdDTOMap.put(product.getId(), product);
            if (product == null) {
                throw new ServiceException("商品并不存在");
            }
            if (product.getKxStockVo().getStock() < orderRequestKxStoreProduct.getCartNum()) {
                throw new ServiceException("商品库存不足～");
            }
            BigDecimal p = BigDecimal.ZERO;
            if (groupShopId != null && groupShopPrice != null) {
                p = groupShopPrice;
            } else {
                p = product.getKxStockVo().getPrice().multiply(BigDecimal.valueOf(orderRequestKxStoreProduct.getCartNum()));
            }
            productPrice = productPrice.add(p);
            productOriginalPrice = productOriginalPrice.add(product.getOtPrice().multiply(BigDecimal.valueOf(orderRequestKxStoreProduct.getCartNum())));
            List<Long> categoryFamily = categoryService.getCategoryFamily(product.getCateId());
            for (Long cid : categoryFamily) {
                BigDecimal price = categoryPriceMap.get(cid);
                if (price == null) {
                    price = p;
                } else {
                    price = price.add(p);
                }
                categoryPriceMap.put(cid, price);
            }
        }
//                  这层判断会提交不了订单
//                if (skuPric!= orderRequest.getTotalPrice()) {
//                    throw new ServiceException(ExceptionDefinition.ORDER_PRICE_CHECK_FAILED);
//                }
        //优惠券折扣价格
        BigDecimal couponPrice = BigDecimal.ZERO;

        //参数强校验 END
        //???是否校验actualPrice??强迫校验？
        BigDecimal actualPrice = productPrice.subtract(couponPrice);
        OrderPriceBo orderPriceBo = new OrderPriceBo();
        orderPriceBo.setGroupShopId(groupShopId);
        orderPriceBo.setActualPrice(actualPrice);

        orderPriceBo.setFreightPrice(BigDecimal.ZERO);
        orderPriceBo.setProductTotalPrice(productPrice);
        orderPriceBo.setProductIdDTOMap(productIdDTOMap);
        orderPriceBo.setProductOriginalTotalPrice(productOriginalPrice);
        return orderPriceBo;
    }

    /**
     * 3.构建订单部分
     *
     * @param orderDO
     * @param orderRequest
     * @param channel
     * @param userId
     */
    @Override
    public void buildOrderHandlePart(KxStoreOrder orderDO, OrderPriceBo orderPriceDTO, OrderRequestBo orderRequest, String channel, Long userId) throws ServiceException {
        orderDO.setTotalPrice(orderPriceDTO.getProductTotalPrice());
        orderDO.setOriginalTotalPrice(orderPriceDTO.getProductOriginalTotalPrice());
        orderDO.setIsChannel(channel);
        orderDO.setPayPrice(orderPriceDTO.getActualPrice());
        orderDO.setCombinationId(orderPriceDTO.getGroupShopId());
        BigDecimal couponPrice = orderPriceDTO.getCouponPrice();
        if (couponPrice != null && couponPrice.compareTo(BigDecimal.ZERO) != 0) {
            orderDO.setCouponId(orderPriceDTO.getCouponId());
            orderDO.setCouponPrice(couponPrice);
        }
        Date now = new Date();
        orderDO.setRemark(orderRequest.getMono());
        orderDO.setFreightPrice(orderPriceDTO.getFreightPrice());
        orderDO.setOrderId(GeneratorUtil.genOrderId());
        orderDO.setUid(userId);
        orderDO.setStatus(OrderStatusType.UNPAY.getCode());
        orderDO.setUpdateTime(now);
        orderDO.setCreateTime(now);
        orderDO.setPredictDate(orderRequest.getPredictDate());
        if (orderRequest.getAddressId() != null) {
            KxAddress address = addressMapper.selectById(orderRequest.getAddressId());
            if (!userId.equals(address.getUserId())) {
                throw new ServiceException("收货地址不属于您！");
            }
            orderDO.setRealName(address.getConsignee());
            orderDO.setUserPhone(address.getPhone());
            orderDO.setProvince(address.getProvince());
            orderDO.setCity(address.getCity());
            orderDO.setCounty(address.getCounty());
            orderDO.setUserAddress(address.getAddress());
            //添加预计送达时间
            orderDO.setPredictTime(orderRequest.getPredictTime());
            orderDO.setStoreId(orderRequest.getStorageId());
        }
        orderMapper.insert(orderDO);
    }

    /**
     * 4.更新优惠券部分
     *
     * @param orderDO
     */
    @Override
    public void buildCoupontHandlePart(KxStoreOrder orderDO) throws ServiceException {
        //扣除用户优惠券
//        if (orderDO.getCouponId() != null) {
//            UserCouponDO updateUserCouponDO = new UserCouponDO();
//            updateUserCouponDO.setId(orderDO.getCouponId());
//            updateUserCouponDO.setGmtUsed(new Date());
//            updateUserCouponDO.setOrderId(orderDO.getId());
//            userCouponMapper.updateById(updateUserCouponDO);
//        }
    }

    /**
     * 5.订单商品SKU部分
     */
    @Override
    public void buildOrderSkuHandlePart(KxStoreOrder orderDO, OrderPriceBo orderPriceDTO, OrderRequestBo orderRequest) {
        KxStoreProductVo productVo;
        KxStoreOrderProduct storeOrderProduct;
        Date now = new Date();
        Map<Long, KxStoreProductVo> productIdDTOMap = orderPriceDTO.getProductIdDTOMap();
        List<KxStoreOrderProduct> orderSkuDOList = new ArrayList<>();
        List<OrderRequestProductBo> skuList = orderRequest.getProductList();
        for (OrderRequestProductBo orderRequestKxStoreProduct : skuList) {
            storeOrderProduct = new KxStoreOrderProduct();
            productVo = productIdDTOMap.get(orderRequestKxStoreProduct.getProductId());
            storeOrderProduct.setBarCode(productVo.getBarCode());
            storeOrderProduct.setProductTitle(productVo.getStoreName());
            storeOrderProduct.setUnitName(productVo.getUnitName());
            storeOrderProduct.setProductAttrTitle(productVo.getStoreName());
            storeOrderProduct.setImg(productVo.getImage() == null ? productVo.getSliderImage() : productVo.getImage());
            storeOrderProduct.setNum(orderRequestKxStoreProduct.getCartNum());
            storeOrderProduct.setOtPrice(productVo.getOtPrice());
            storeOrderProduct.setPrice(productVo.getKxStockVo().getPrice());
            storeOrderProduct.setProductAttrId(productVo.getId());
            storeOrderProduct.setProductId(productVo.getId());
            storeOrderProduct.setOrderNo(orderDO.getOrderId());
            storeOrderProduct.setOrderId(orderDO.getId());
            storeOrderProduct.setCreateTime(now);
            storeOrderProduct.setUpdateTime(now);
            orderSkuDOList.add(storeOrderProduct);
            //扣除库存
            productMapper.decSkuStock(orderRequestKxStoreProduct.getProductId(), orderRequestKxStoreProduct.getCartNum(), orderDO.getStoreId());
        }
        orderProductMapper.insertBatch(orderSkuDOList);
    }

    /**
     * 6.购物车部分
     *
     * @param orderRequest
     * @param userId
     */
    @Override
    public void buildCartHandlePart(OrderRequestBo orderRequest, Long userId) {
        List<OrderRequestProductBo> skuList = orderRequest.getProductList();
        if (!StringUtils.isEmpty(orderRequest.getTakeWay())) {
            String takeWay = orderRequest.getTakeWay();
            if ("cart".equals(takeWay)) {
                //扣除购物车
                List<Long> productIds = skuList.stream().map(item -> item.getProductId()).collect(Collectors.toList());
                cartMapper.delete(new QueryWrapper<KxStoreCart>().in("product_id", productIds).eq("uid", userId));
            }
            //直接购买传值为 "buy"
        }
    }

    /**
     * 7.触发订单创建完成后通知事件部分
     *
     * @param orderDO
     */
    @Override
    public void buildNotifyHandlePart(KxStoreOrder orderDO) {
        OrderUpdater updater = new OrderUpdater(orderDO);
        observeAble.notifyObservers(updater);
    }
}
