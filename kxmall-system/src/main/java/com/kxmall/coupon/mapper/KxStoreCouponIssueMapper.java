package com.kxmall.coupon.mapper;

import com.kxmall.common.core.mapper.BaseMapperPlus;
import com.kxmall.coupon.domain.KxStoreCouponIssue;
import com.kxmall.coupon.domain.vo.KxStoreCouponIssueVo;

/**
 * 优惠券前台领取Mapper接口
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-02-17
 */
public interface KxStoreCouponIssueMapper extends BaseMapperPlus<KxStoreCouponIssueMapper, KxStoreCouponIssue, KxStoreCouponIssueVo> {


    void decCoupon(Long couponId);
}
