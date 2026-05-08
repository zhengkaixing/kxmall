package com.kxmall.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import com.kxmall.user.domain.KxUserBill;
import com.kxmall.user.domain.vo.KxUserBillVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户账单Mapper接口
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-02-14
 */
public interface KxUserBillMapper extends BaseMapperPlus<KxUserBillMapper, KxUserBill, KxUserBillVo> {


    Page<KxUserBillVo> selectVoPageList(@Param("page") Page<Object> build, @Param(Constants.WRAPPER) QueryWrapper<KxUserBill> lqw);

    @Select("select IFNULL(sum(number),0) from kx_user_bill " +
            "where status=1 and type='sign' and pm=1 and category='integral' " +
            "and uid=#{uid}")
    double sumIntegral(@Param("uid") Long uid);

    @Select("select IFNULL(sum(number),0) from kx_user_bill " +
            "where status=1 and type='brokerage' and pm=1 and category='now_money' " +
            "and uid=#{userId} and TO_DAYS(NOW()) - TO_DAYS(create_time) <= 1")
    double sumYesterdayPrice(@Param("userId") Long userId);
}
