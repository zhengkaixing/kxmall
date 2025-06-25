package com.kxmall.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import com.kxmall.user.domain.KxUserSign;
import com.kxmall.user.domain.vo.KxUserSignVo;
import org.apache.ibatis.annotations.Param;

/**
 * 签到记录Mapper接口
 *
 * @author 郅兴开源团队-小黑
 * @date 2024-08-26
 */
public interface KxUserSignMapper extends BaseMapperPlus<KxUserSignMapper, KxUserSign, KxUserSignVo> {

    Page<KxUserSignVo> selectVoPageList(@Param("page") Page<Object> build, @Param(Constants.WRAPPER) QueryWrapper<KxUserSign> lqw);

}
