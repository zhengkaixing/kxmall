package com.kxmall.coupon.mapper;

import com.kxmall.common.core.mapper.BaseMapperPlus;
import com.kxmall.coupon.domain.KxStoreCouponUser;
import com.kxmall.coupon.domain.vo.KxStoreCouponIssueVo;
import com.kxmall.coupon.domain.vo.KxStoreCouponUserVo;
import com.kxmall.model.KVModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * 优惠券发放记录Mapper接口
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-02-17
 */
public interface KxStoreCouponUserMapper extends BaseMapperPlus<KxStoreCouponUserMapper, KxStoreCouponUser, KxStoreCouponUserVo> {

    List<KxStoreCouponUserVo> getUserCoupons(@Param("userId") Long userId, @Param("status") Integer status);

    List<KxStoreCouponIssueVo> getActiveCoupons();

    List<KVModel<BigInteger, Long>> getUserCouponsCount(@Param("userId") Long userId, @Param("couponIds") List<Long> activeCouponIds);
}
