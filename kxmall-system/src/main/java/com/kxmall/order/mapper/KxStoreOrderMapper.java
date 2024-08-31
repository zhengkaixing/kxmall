package com.kxmall.order.mapper;

import com.kxmall.dashboard.domain.SalesStatementDTO;
import com.kxmall.dashboard.domain.SalesTopDTO;
import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.domain.vo.KxStoreOrderVo;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 订单Mapper接口
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-02-15
 */
public interface KxStoreOrderMapper extends BaseMapperPlus<KxStoreOrderMapper, KxStoreOrder, KxStoreOrderVo> {

    List<KxStoreOrderVo> selectOrderPages(@Param("status") List<Integer> status, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("userId") Long userId);

    Long countOrders(@Param("status") List<Integer> status, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("userId") Long userId);

    /**
     * 获取一级类目销量排名
     *
     * @return
     */
    List<SalesStatementDTO> getSalesCategoryRank(@Param("storageId") Long storageId, @Param("storageIds") Set<Long> storageIds);

    /**
     * 获取商品销量排名
     *
     * @return
     */
    List<SalesTopDTO> getSalesCategoryRanTopFive(@Param("categoryId") Long categoryId, @Param("storageId") Long storageId, @Param("storageIds") Set<Long> storageIds);

    /**
     * 今日销量
     *
     * @return
     */
    SalesStatementDTO getTodaySales(@Param("storageId") Long storageId, @Param("storageIds") Set<Long> storageIds);

    /**
     * 昨日销量
     *
     * @return
     */
    SalesStatementDTO getYesterdaySales(@Param("storageId") Long storageId, @Param("storageIds") Set<Long> storageIds);

    /**
     * 按两小时统计销量
     *
     * @return
     */
    SalesStatementDTO getSalesByHour(@Param("storageId") Long storageId, @Param("start") Date start, @Param("end") Date end, @Param("storageIds") Set<Long> storageIds);


    List<String> selectExpireOrderNos(@Param("status") Integer status, @Param("time") Date time);


}
