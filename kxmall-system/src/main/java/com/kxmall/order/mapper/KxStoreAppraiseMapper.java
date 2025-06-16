package com.kxmall.order.mapper;

import com.kxmall.common.core.mapper.BaseMapperPlus;
import com.kxmall.order.domain.KxStoreAppraise;
import com.kxmall.order.domain.vo.KxStoreAppraiseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论管理Mapper接口
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-08-27
 */
public interface KxStoreAppraiseMapper extends BaseMapperPlus<KxStoreAppraiseMapper, KxStoreAppraise, KxStoreAppraiseVo> {

    /**
     * 获取评论
     * @param productId
     * @param offset
     * @param pageSize
     * @param state
     * @return
     */
    List<KxStoreAppraiseVo> selectProductAppraiseByPage(@Param("productId") Long productId, @Param("offset") Integer offset, @Param("size") Integer pageSize, @Param("state") Integer state);
}
