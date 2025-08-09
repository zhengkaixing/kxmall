package com.kxmall.web.controller.order.builder;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kxmall.address.domain.KxAddress;
import com.kxmall.address.mapper.KxAddressMapper;
import com.kxmall.common.enums.*;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.GeneratorUtil;
import com.kxmall.coupon.domain.KxStoreCoupon;
import com.kxmall.coupon.domain.KxStoreCouponUser;
import com.kxmall.coupon.mapper.KxStoreCouponMapper;
import com.kxmall.coupon.mapper.KxStoreCouponUserMapper;
import com.kxmall.executor.GlobalExecutor;
import com.kxmall.group.biz.GroupShopBizService;
import com.kxmall.group.domain.KxGroupShopProduct;
import com.kxmall.group.domain.vo.KxGroupShopVo;
import com.kxmall.group.mapper.KxGroupShopMapper;
import com.kxmall.notify.AdminNotifyBizService;
import com.kxmall.order.domain.KxStoreCart;
import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.domain.KxStoreOrderProduct;
import com.kxmall.order.domain.bo.OrderPriceBo;
import com.kxmall.order.domain.bo.OrderRequestBo;
import com.kxmall.order.domain.bo.OrderRequestProductBo;
import com.kxmall.order.domain.vo.KxStoreOrderProductVo;
import com.kxmall.order.domain.vo.KxStoreOrderVo;
import com.kxmall.order.mapper.KxStoreCartMapper;
import com.kxmall.order.mapper.KxStoreOrderMapper;
import com.kxmall.order.mapper.KxStoreOrderProductMapper;
import com.kxmall.orderobserve.OrderObserveAble;
import com.kxmall.orderobserve.OrderUpdater;
import com.kxmall.print.AdminPrintBizService;
import com.kxmall.product.domain.vo.KxStoreProductVo;
import com.kxmall.product.mapper.KxStoreProductMapper;
import com.kxmall.storage.domain.KxStorage;
import com.kxmall.storage.mapper.KxStorageMapper;
import com.kxmall.user.domain.KxUser;
import com.kxmall.user.domain.KxUserBill;
import com.kxmall.user.mapper.KxUserBillMapper;
import com.kxmall.user.mapper.KxUserMapper;
import com.kxmall.web.controller.order.service.IKxAppOrderService;
import com.kxmall.web.controller.product.service.IKxAppCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
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
    private KxStorageMapper storageMapper;

    @Autowired
    private KxAddressMapper addressMapper;

    @Resource
    private OrderObserveAble observeAble;

    @Autowired
    private KxStoreOrderProductMapper orderProductMapper;

    @Autowired
    private IKxAppOrderService appOrderService;

    @Autowired
    private IKxAppCategoryService categoryService;

    @Autowired
    private KxStoreCouponUserMapper couponUserMapper;
    @Autowired
    private KxStoreCouponMapper couponMapper;

    @Autowired
    private GroupShopBizService groupShopBizService;

    @Autowired
    private KxUserMapper userMapper;

    @Autowired
    private AdminNotifyBizService adminNotifyBizService;

    @Autowired
    private AdminPrintBizService adminPrintBizService;

    @Autowired
    private KxGroupShopMapper groupShopMapper;

    @Autowired
    private KxStoreProductMapper storeProductMapper;

    @Autowired
    private KxUserBillMapper userBillMapper;

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
        if (orderRequest.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0 && orderRequest.getIntegral() == 0L) {
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
        if (groupShopId != null) {
            //校验团购参数
            if (skuList.size() > 1) {
                throw new ServiceException("团购订单只允许单品结算");
            }
            KxGroupShopVo groupShopDTO = groupShopBizService.getGroupShopById(groupShopId, null);
            if (groupShopDTO == null || groupShopDTO.getStatus() == StatusType.LOCK.getCode()) {
                throw new ServiceException("团购活动已经过期或不存在");
            }
            List<KxGroupShopProduct> groupShopSkuList = groupShopDTO.getGroupShopSkuList();
            for (KxGroupShopProduct groupShopSkuDO : groupShopSkuList) {
                if (groupShopSkuDO.getProductAttrId().equals(groupShopSkuList.get(0).getProductAttrId())) {
                    //若找到交集
                    groupShopPrice = groupShopSkuDO.getProductGroupShopPrice();
                }
            }
        }
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
            if (product == null) {
                throw new ServiceException("商品并不存在");
            }
            productIdDTOMap.put(product.getId(), product);
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
        //优惠券折扣价格
        BigDecimal couponPrice = BigDecimal.ZERO;
        //优惠券校验
        KxStoreCouponUser userCouponFromFront = orderRequest.getCoupon();
        if (userCouponFromFront != null) {
            if (userCouponFromFront.getId() == null || userCouponFromFront.getCouponPrice() == null) {
                throw new ServiceException("参数校验失败");
            }

            KxStoreCoupon userCouponFromDB = couponMapper.selectById(userCouponFromFront.getCid());

            if (userCouponFromDB == null) {
                throw new ServiceException("优惠券不存在或已使用！");
            }

            if (!userCouponFromDB.getCouponPrice().equals(userCouponFromFront.getCouponPrice())) {
                throw new ServiceException("订单优惠券金额校验失败");
            }
            //校验优惠券策略是否满足
            Long categoryId = userCouponFromDB.getCategoryId();
            if (categoryId != null && categoryId != 0L) {
                BigDecimal p = categoryPriceMap.get(categoryId);
                if (p.compareTo(userCouponFromDB.getUseMinPrice()) < 0) {
                    throw new ServiceException("优惠券金额未达到指定值");
                }
            } else {
                if (productPrice.compareTo(userCouponFromDB.getUseMinPrice()) < 0) {
                    throw new ServiceException("优惠券金额未达到指定值");
                }
            }
            couponPrice = userCouponFromDB.getCouponPrice();
        }
        BigDecimal freightPrice = BigDecimal.ZERO;
        //参数强校验 END
        //???是否校验actualPrice??强迫校验？
        BigDecimal actualPrice = productPrice.subtract(couponPrice).add(freightPrice);
        OrderPriceBo orderPriceBo = new OrderPriceBo();
        orderPriceBo.setGroupShopId(groupShopId);
        orderPriceBo.setActualPrice(actualPrice);
        //秒杀
        orderPriceBo.setFreightPrice(freightPrice);
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
        orderDO.setSeckillId(orderPriceDTO.getSeckillId());
        KxStoreCouponUser coupon = orderRequest.getCoupon();
        if (coupon != null && coupon.getCouponPrice().compareTo(BigDecimal.ZERO) != 0) {
            orderDO.setCouponId(coupon.getId());
            orderDO.setCouponPrice(coupon.getCouponPrice());
        }
        Date now = new Date();
        orderDO.setRemark(orderRequest.getMono());
        orderDO.setFreightPrice(orderPriceDTO.getFreightPrice());
        orderDO.setOrderId(GeneratorUtil.genOrderId());
        orderDO.setUid(userId);
        orderDO.setPayChannel(orderRequest.getPayType());
        orderDO.setStatus(OrderStatusType.UNPAY.getCode());
        orderDO.setUpdateTime(now);
        orderDO.setCreateTime(now);
        orderDO.setPredictDate(orderRequest.getPredictDate());
        orderDO.setShippingType(orderRequest.getShippingType());

        Boolean isIntegral = false;
        //计算奖励积分
        BigDecimal gainIntegral = this.calculateBonusPoints(orderPriceDTO,orderRequest.getProductList());
        if (PayChannelType.INTEGRAL.getCode().equals(orderRequest.getPayType())) {
            gainIntegral = BigDecimal.ZERO;
            isIntegral = true;
        }
        orderDO.setGainIntegral(gainIntegral);

        //自提
        if (orderRequest.getStorageId() != null && orderRequest.getShippingType() == 2) {
            KxStorage storage = storageMapper.selectById(orderRequest.getStorageId());
            orderDO.setLatitude(storage.getLatitude());
            orderDO.setLongitude(storage.getLongitude());
            orderDO.setRealName(orderRequest.getName());
            orderDO.setUserPhone(orderRequest.getPhone());
            orderDO.setProvince(storage.getProvince() + "");
            orderDO.setCity(storage.getCity() + "");
            orderDO.setCounty(storage.getCounty() + "");
            orderDO.setUserAddress(storage.getAddress());
            //添加预计送达时间
            orderDO.setPredictTime(orderRequest.getPredictTime());
            orderDO.setStoreId(orderRequest.getStorageId());
        }

        if (orderRequest.getAddressId() != null && orderRequest.getShippingType() == 1) {
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
            orderDO.setLatitude(address.getLatitude());
            orderDO.setLongitude(address.getLongitude());
        }

        //余额结算额外逻辑
        if (PayChannelType.BALANCE.getCode().equals(orderRequest.getPayType())) {
            KxUser kxUser = userMapper.selectById(userId);
            if (orderPriceDTO.getActualPrice().compareTo(kxUser.getNowMoney()) > 0) {
                throw new ServiceException("余额不足！");
            }

            orderDO.setPayId("0");
            orderDO.setPayTime(new Date());
            if (orderDO.getCombinationId() != null && orderDO.getCombinationId() != 0L) {
                orderDO.setStatus(OrderStatusType.GROUP_SHOP_WAIT.getCode());
            } else {
                orderDO.setStatus(OrderStatusType.WAIT_PREPARE_GOODS.getCode());
            }
        }else if(PayChannelType.INTEGRAL.getCode().equals(orderRequest.getPayType())){
            orderDO.setUseIntegral(BigDecimal.valueOf(orderRequest.getIntegral()));
            orderDO.setPayIntegral(BigDecimal.valueOf(orderRequest.getIntegral()));
            //配送费默认0
            orderDO.setFreightPrice(BigDecimal.ZERO);
            //实付金额为0
            orderDO.setPayPrice(BigDecimal.ZERO);
            KxUser kxUser = userMapper.selectById(userId);
            if (orderRequest.getIntegral()>kxUser.getIntegral().longValue()) {
                throw new ServiceException("积分不足！");
            }
            orderDO.setPayId("0");
            orderDO.setPayTime(new Date());
            orderDO.setStatus(OrderStatusType.WAIT_PREPARE_GOODS.getCode());
        }

        orderMapper.insert(orderDO);
    }

    private BigDecimal calculateBonusPoints(OrderPriceBo orderPriceBo,List<OrderRequestProductBo> productList) {
        BigDecimal gainIntegral = BigDecimal.ZERO;
        for (OrderRequestProductBo requestProductBo : productList) {
            if (ObjectUtils.isEmpty(orderPriceBo.getGroupShopId())) {
                orderPriceBo.setGroupShopId(0L);
            }
            if (ObjectUtils.isEmpty(orderPriceBo.getSeckillId())) {
                orderPriceBo.setSeckillId(0L);
            }
            if (orderPriceBo.getGroupShopId() > 0 || orderPriceBo.getSeckillId() > 0) {
                continue;
            }
            BigDecimal cartInfoGainIntegral = BigDecimal.ZERO;
            if (ObjectUtils.isEmpty(requestProductBo.getGiveIntegral())) {
                return BigDecimal.ZERO;
            }
            Long gain = requestProductBo.getGiveIntegral().longValue();
            if (gain > 0) {
                cartInfoGainIntegral = NumberUtil.round(NumberUtil.mul(requestProductBo.getCartNum(), gain), 2);
            }
            gainIntegral = NumberUtil.add(gainIntegral, cartInfoGainIntegral);
        }
        return gainIntegral;
    }

    /**
     * 4.更新优惠券部分
     *
     * @param orderDO
     */
    @Override
    public void buildCoupontHandlePart(KxStoreOrder orderDO) throws ServiceException {
        //扣除用户优惠券
        if (orderDO.getCouponId() != null) {
            KxStoreCouponUser updateUserCouponDO = new KxStoreCouponUser();
            updateUserCouponDO.setId(orderDO.getCouponId());
            updateUserCouponDO.setStatus(CouponStatusEnum.STATUS_1.getValue());
            updateUserCouponDO.setUseTime(new Date());
            couponUserMapper.updateById(updateUserCouponDO);
        }
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
            storeOrderProduct.setPrice(orderRequestKxStoreProduct.getPrice());
            storeOrderProduct.setProductAttrId(productVo.getId());
            storeOrderProduct.setProductId(productVo.getId());
            storeOrderProduct.setOrderNo(orderDO.getOrderId());
            storeOrderProduct.setOrderId(orderDO.getId());
            storeOrderProduct.setCreateTime(now);
            storeOrderProduct.setUpdateTime(now);
            orderSkuDOList.add(storeOrderProduct);
            //扣除库存
            Long seckillId = orderDO.getSeckillId();
            if (seckillId != null && seckillId > 0) {
                //秒杀库存扣减
                Integer integer = productMapper.decSeckillStock(orderRequestKxStoreProduct.getProductId(), seckillId, orderRequestKxStoreProduct.getCartNum(), orderDO.getStoreId());
                if (integer == 0) {
                    throw new ServiceException("秒杀商品库存不足");
                }
            }
            //秒杀的都出后，仍需扣除仓库的库存
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

    @Override
    public void buildCallBackHandlePart(KxStoreOrder orderDO) {
        List<KxStoreOrderVo> KxStoreOrderList = appOrderService.selectListVoByWrapper(
                new QueryWrapper<KxStoreOrder>()
                        .eq("order_id", orderDO.getOrderId()));

        if (CollectionUtils.isEmpty(KxStoreOrderList)) {
            throw new ServiceException("订单不存在 orderNo=" + orderDO.getOrderId());
        }

        KxStoreOrderVo order = KxStoreOrderList.get(0);

        KxUser kxUser = userMapper.selectById(orderDO.getUid());
        //扣款
        KxUser userUpdate = new KxUser();
        userUpdate.setNowMoney(kxUser.getNowMoney().subtract(orderDO.getPayPrice()));
        userMapper.update(userUpdate, new LambdaQueryWrapper<KxUser>().eq(KxUser::getNowMoney, kxUser.getNowMoney())
                .eq(KxUser::getUid, kxUser.getUid()));

        //扣款记录
        String mark = "系统扣除了" + order.getPayPrice().toString() + "余额";
        Double newMoney = NumberUtil.sub(kxUser.getNowMoney(), order.getPayPrice()).doubleValue();
        if (newMoney < 0) {
            newMoney = 0d;
        }

        KxUserBill userBill = KxUserBill.builder()
                .uid(kxUser.getUid())
                .title("消费减少余额")
                .category(BillDetailEnum.CATEGORY_1.getValue())
                .type(BillDetailEnum.TYPE_3.getValue())
                .number(order.getPayPrice())
                .balance(BigDecimal.valueOf(newMoney))
                .mark(mark)
                .pm(BillEnum.PM_0.getValue())
                .build();
        userBillMapper.insert(userBill);

        //**************** 在此之前都没有 数据库修改 操作 所以前面是直接返回错误的 **********************//

        List<KxStoreOrderProductVo> orderProducts = orderProductMapper.selectVoList(new QueryWrapper<KxStoreOrderProduct>().eq("order_id", order.getId()));
        order.setProductList(orderProducts);
        orderProducts.forEach(item -> {
            //增加销量
            storeProductMapper.incSales(item.getProductId(), item.getNum());
            if (order.getCombinationId() != null) {
                //增加团购人数, 若想算商品数这里就获取orderSku的数量，若想算人数，这里就写1
                groupShopMapper.incCurrentNum(order.getCombinationId(), item.getNum());
            }
        });

        //通知管理员发货
        GlobalExecutor.execute(() -> {
            adminNotifyBizService.newOrder(order);
            adminNotifyBizService.newOrder(order);
            adminPrintBizService.newOrderPrint(order);
        });
    }

    @Override
    public void buildCallBackHandlePointsPart(KxStoreOrder orderDO) {
        List<KxStoreOrderVo> KxStoreOrderList = appOrderService.selectListVoByWrapper(
                new QueryWrapper<KxStoreOrder>()
                        .eq("order_id", orderDO.getOrderId()));

        if (CollectionUtils.isEmpty(KxStoreOrderList)) {
            throw new ServiceException("订单不存在 orderNo=" + orderDO.getOrderId());
        }

        KxStoreOrderVo order = KxStoreOrderList.get(0);

        KxUser kxUser = userMapper.selectById(orderDO.getUid());
        //扣款
        KxUser userUpdate = new KxUser();
        userUpdate.setIntegral(kxUser.getIntegral().subtract(orderDO.getUseIntegral()));
        userMapper.update(userUpdate, new LambdaQueryWrapper<KxUser>().eq(KxUser::getIntegral, kxUser.getIntegral())
                .eq(KxUser::getUid, kxUser.getUid()));

        //扣款记录
        String mark = "系统扣除了" + order.getUseIntegral().toString() + "积分";
        Double newIntegral = NumberUtil.sub(kxUser.getIntegral(), order.getUseIntegral()).doubleValue();
        if (newIntegral < 0) {
            newIntegral = 0d;
        }

        KxUserBill userBill = KxUserBill.builder()
                .uid(kxUser.getUid())
                .title("消费减少积分")
                .category(BillDetailEnum.CATEGORY_2.getValue())
                .type(BillDetailEnum.TYPE_3.getValue())
                .number(order.getUseIntegral())
                .balance(BigDecimal.valueOf(newIntegral))
                .mark(mark)
                .pm(BillEnum.PM_0.getValue())
                .build();
        userBillMapper.insert(userBill);

        //**************** 在此之前都没有 数据库修改 操作 所以前面是直接返回错误的 **********************//

        List<KxStoreOrderProductVo> orderProducts = orderProductMapper.selectVoList(new QueryWrapper<KxStoreOrderProduct>().eq("order_id", order.getId()));
        order.setProductList(orderProducts);
        orderProducts.forEach(item -> {
            //增加销量
            storeProductMapper.incSales(item.getProductId(), item.getNum());
        });

        //通知管理员发货
        GlobalExecutor.execute(() -> {
            adminNotifyBizService.newOrder(order);
            adminPrintBizService.newOrderPrint(order);
        });
    }
}
