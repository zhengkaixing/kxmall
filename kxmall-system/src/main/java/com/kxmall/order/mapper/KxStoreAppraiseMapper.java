package com.kxmall.order.mapper;

import com.kxmall.order.domain.KxStoreAppraise;
import com.kxmall.order.domain.vo.KxStoreAppraiseVo;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论管理Mapper接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface KxStoreAppraiseMapper extends BaseMapperPlus<KxStoreAppraiseMapper, KxStoreAppraise, KxStoreAppraiseVo> {

    /**
     * 获取评论
     * @param productId
     * @param offset
     * @param pageSize
     * @return
     */
    List<KxStoreAppraiseVo> selectProductAppraiseByPage(@Param("productId") Long productId, @Param("offset") Integer offset, @Param("size") Integer pageSize);
}
