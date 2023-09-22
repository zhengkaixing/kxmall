package com.kxmall.order.mapper;

import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.domain.vo.KxStoreOrderVo;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单Mapper接口
 *
 * @author kxmall
 * @date 2023-02-15
 */
public interface KxStoreOrderMapper extends BaseMapperPlus<KxStoreOrderMapper, KxStoreOrder, KxStoreOrderVo> {

    List<KxStoreOrderVo> selectOrderPages(@Param("status") List<Integer> status, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("userId") Long userId);

    Long countOrders(@Param("status") List<Integer> status, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("userId") Long userId);
}
