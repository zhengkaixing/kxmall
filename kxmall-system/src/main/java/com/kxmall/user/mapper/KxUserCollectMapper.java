package com.kxmall.user.mapper;

import com.kxmall.user.domain.KxUserCollect;
import com.kxmall.user.domain.vo.KxUserCollectVo;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户收藏Mapper接口
 *
 * @author kxmall
 * @date 2023-04-06
 */
public interface KxUserCollectMapper extends BaseMapperPlus<KxUserCollectMapper, KxUserCollect, KxUserCollectVo> {

    List<KxUserCollectVo> getCollectAll(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("size") Integer size);

    Long getCollectAllByCount(@Param("userId") Long userId);
}
