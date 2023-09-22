package com.kxmall.recommend.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import com.kxmall.recommend.domain.KxRecommend;
import com.kxmall.recommend.domain.vo.KxRecommendVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 推荐管理Mapper接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface KxRecommendMapper extends BaseMapperPlus<KxRecommendMapper, KxRecommend, KxRecommendVo> {

    /**
     * 获取指定仓库推荐下的商品
     *
     * @param storageId
     * @param recommendType
     * @param offset
     * @param size
     * @return
     */
    List<KxRecommendVo> getRecommendByStorage(@Param("storageId") Long storageId, @Param("recommendType") Integer recommendType, @Param("offset") Integer offset, @Param("size") Integer size);

    Long getRecommendByStorageCount(@Param("storageId") Long storageId, @Param("recommendType") Integer recommendType);

    Page<KxRecommendVo> selectVoPageBySQL(@Param("page") Page<KxRecommend> page, @Param(Constants.WRAPPER) Wrapper<KxRecommend> lqw);
}
