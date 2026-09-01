package com.kxmall.web.controller.order;

import com.kxmall.common.annotation.RateLimiter;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.OrderStatusType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.domain.bo.OrderRequestBo;
import com.kxmall.order.domain.vo.KxStoreOrderVo;
import com.kxmall.storage.domain.vo.KxStorageVo;
import com.kxmall.web.controller.order.builder.OrderBuilder;
import com.kxmall.web.controller.order.builder.OrderDirector;
import com.kxmall.web.controller.order.service.IKxAppOrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * APP端订单管理控制器
 * 提供订单提交、支付、查询、取消、确认、退款等功能
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/order/app")
public class KxAppOrderController extends BaseAppController {

    /**
     * 订单构建器
     * 使用建造者模式构建订单对象
     */
    private final OrderBuilder orderBuilder;

    /**
     * 订单服务接口
     */
    private final IKxAppOrderService appOrderService;

    /**
     * 提交订单分布式锁前缀
     * 用于防止用户重复提交订单
     */
    private static final String TAKE_ORDER_LOCK = "TAKE_ORDER_";

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(KxAppOrderController.class);

    /**
     * 提交订单
     * 使用分布式锁和限流注解防止重复提交和并发问题
     * 使用事务保证数据一致性
     *
     * @param orderRequest 订单请求对象，包含商品信息、收货地址等
     * @return R<String> 订单ID
     */
    @PostMapping("/takeOrder")
    @Transactional(rollbackFor = Exception.class)
    @RateLimiter(count = 2) // 限流：每秒最多2次请求
    public R<String> takeOrder(@RequestBody OrderRequestBo orderRequest) {
        // 获取当前登录用户ID
        Long userId = getAppLoginUser().getUserId();
        // 获取分布式锁，防止同一用户重复提交订单
        Lock lock = RedisUtils.lock(TAKE_ORDER_LOCK + userId);
        boolean isLocked;
        try {
            // 尝试获取锁，最多等待20秒
            isLocked = lock.tryLock(20, TimeUnit.SECONDS);
            if (isLocked) {
                try {
                    // 创建订单对象
                    KxStoreOrder orderDO = KxStoreOrder.builder().build();
                    // 使用建造者模式构建订单
                    OrderDirector orderDirector = new OrderDirector(orderBuilder);
                    orderDirector.constructOrder(orderDO, orderRequest, orderRequest.getChannel(), userId);
                    // 返回订单ID
                    return R.ok(orderDO.getOrderId());
                } catch (ServiceException e) {
                    // 业务异常直接抛出
                    throw e;
                } catch (Exception e) {
                    // 其他异常记录日志并抛出
                    logger.error("[提交订单] 异常", e);
                    throw new ServiceException("订单系统未知异常");
                } finally {
                    // 释放锁
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            // 获取锁被中断
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
        // 获取锁失败，系统繁忙
        throw new ServiceException("订单系统繁忙~");
    }

    /**
     * 微信小程序预支付
     * 生成微信支付所需的预支付参数
     *
     * @param orderId 订单ID
     * @return R<Object> 预支付参数对象
     */
    @GetMapping("/wxPrepay")
    @Transactional(rollbackFor = Exception.class)
    public R<Object> wxPrepay(String orderId) {
        // 获取当前登录用户信息
        Long userId = getAppLoginUser().getUserId();
        Integer loginType = getAppLoginUser().getLoginType();
        String openId = getAppLoginUser().getOpenId();
        // 调用服务生成预支付参数
        return R.ok(appOrderService.wxPrepay(orderId, userId, loginType, openId));
    }

    /**
     * 分页查询订单列表
     * 根据订单状态筛选当前用户的订单
     *
     * @param pageNo 页码，默认1
     * @param pageSize 每页数量，默认10
     * @param status 订单状态（可选）
     * @return TableDataInfo<KxStoreOrderVo> 分页订单数据
     */
    @GetMapping("/getOrderPage")
    public TableDataInfo<KxStoreOrderVo> getOrderPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                                       String status) {
        // 获取当前登录用户ID
        Long userId = getAppLoginUser().getUserId();
        // 查询订单列表
        return appOrderService.getOrderPage(pageNo, pageSize, status, userId);
    }

    /**
     * 取消订单
     * 用户主动取消未支付的订单
     *
     * @param orderId 订单ID
     * @return R<String> 操作结果消息
     */
    @GetMapping("/cancel")
    public R<String> cancel(String orderId) {
        // 获取当前登录用户ID
        Long userId = getAppLoginUser().getUserId();
        // 执行取消订单操作
        return R.ok(appOrderService.cancel(orderId, userId));
    }

    /**
     * 确认收货
     * 用户确认收到商品，订单状态变为已完成
     *
     * @param orderId 订单ID
     * @return R<String> 操作结果消息
     */
    @GetMapping("/confirm")
    public R<String> confirm(String orderId) {
        // 获取当前登录用户ID
        Long userId = getAppLoginUser().getUserId();
        // 执行确认收货操作
        return R.ok(appOrderService.confirm(orderId, userId));
    }

    /**
     * 获取订单详情
     * 查询指定订单的详细信息
     *
     * @param orderId 订单ID
     * @return R<KxStoreOrderVo> 订单详情对象
     */
    @GetMapping("/getOrderDetail")
    public R<KxStoreOrderVo> getOrderDetail(Long orderId) {
        // 获取当前登录用户ID
        Long userId = getAppLoginUser().getUserId();
        // 查询订单详情（只能查询自己的订单）
        return R.ok(appOrderService.getOrderDetail(orderId, userId));
    }

    /**
     * 用户申请退款
     * 用户对已支付的订单申请退款
     *
     * @param orderId 订单ID
     * @return R<String> 操作结果消息
     */
    @GetMapping("/refund")
    public R<String> refund(String orderId) {
        // 获取当前登录用户ID
        Long userId = getAppLoginUser().getUserId();
        // 执行退款申请操作
        return R.ok("操作成功！", appOrderService.refund(orderId, userId));
    }

    /**
     * 生成随机的经度
     * 经度范围：-180 到 180
     * 注意：此方法当前未被使用，可能是预留功能
     *
     * @return Double 随机经度值
     */
    private Double generateRandomLongitude() {
        return ThreadLocalRandom.current().nextDouble(-180, 180);
    }

    /**
     * 生成随机的纬度
     * 纬度范围：-90 到 90
     * 注意：此方法当前未被使用，可能是预留功能
     *
     * @return Double 随机纬度值
     */
    private Double generateRandomLatitude() {
        return ThreadLocalRandom.current().nextDouble(-90, 90);
    }

}
