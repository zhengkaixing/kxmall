package com.kxmall.storage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.storage.domain.KxStock;
import com.kxmall.storage.domain.bo.WarningStockBo;
import com.kxmall.storage.domain.vo.KxStockVo;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 前置仓商品Mapper接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface KxStockMapper extends BaseMapperPlus<KxStockMapper, KxStock, KxStockVo> {

    /**
     * 查询预警信息
     * @param build
     * @param bo
     * @return
     */
    List<KxStockVo> warningListByStoragePage(@Param("offset") Integer offset, @Param("size") Integer size, @Param("bo") WarningStockBo bo);

    /**
     * 警告列表
     * @param bo
     * @return
     */
    Long warningListByStoragePageCount(@Param("bo") WarningStockBo bo);

    /**
     * 更新库存
     * @param storageId
     * @param productAttrId
     * @param inStockNum
     * @return
     */
    Integer updateSockForAdd(@Param("storageId") Long storageId, @Param("productAttrId") Long productAttrId, @Param("stockNum") Long inStockNum);

    /**
     * 出库
     * @param storageId
     * @param productAttrId
     * @param inStockNum
     * @return
     */
    Integer updateSock(@Param("storageId") Long storageId, @Param("productAttrId") Long productAttrId, @Param("stockNum") Long inStockNum);

    /**
     * 分页查询
     * @param offset
     * @param size
     * @param storageId
     * @param categoryId
     * @param keyword
     * @param status
     * @param notIds
     * @return
     */
    List<KxStockVo> selectVoBySQL(@Param("offset") Integer offset, @Param("size") Integer size, @Param("storageId") Long storageId, @Param("categoryId") Long categoryId, @Param("keyword") String keyword, @Param("status") Integer status, @Param("notIds") List<Long> notIds);

    /**
     * 计算总数
     * @param storageId
     * @param categoryId
     * @param keyword
     * @param status
     * @param notIds
     * @return
     */
    Long selectVoBySQLCount(@Param("storageId") Long storageId, @Param("categoryId") Long categoryId, @Param("keyword") String keyword, @Param("status") Integer status, @Param("notIds") List<Long> notIds);


}
