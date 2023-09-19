package com.kxmall.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.user.domain.vo.KxUserBillVo;
import com.kxmall.user.domain.KxUserBill;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

/**
 * 用户账单Mapper接口
 *
 * @author kxmall
 * @date 2023-02-14
 */
public interface KxUserBillMapper extends BaseMapperPlus<KxUserBillMapper, KxUserBill, KxUserBillVo> {


    Page<KxUserBillVo> selectVoPageList(@Param("page") Page<Object> build, @Param(Constants.WRAPPER) QueryWrapper<KxUserBill> lqw);
}
