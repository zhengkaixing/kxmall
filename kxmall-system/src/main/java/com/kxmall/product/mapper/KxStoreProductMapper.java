package com.kxmall.product.mapper;

import com.kxmall.common.core.mapper.BaseMapperPlus;
import com.kxmall.product.domain.KxStoreProduct;
import com.kxmall.product.domain.vo.KxStoreProductVo;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;

/**
 * 商品Mapper接口
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-02-13
 */
public interface KxStoreProductMapper extends BaseMapperPlus<KxStoreProductMapper, KxStoreProduct, KxStoreProductVo> {

    List<KxStoreProductVo> selectPageByStorage(@Param("offset") Integer offset,
                                               @Param("size") Integer size,
                                               @Param("title") String title,
                                               @Param("categoryId") Long categoryId,
                                               @Param("childrenIds") LinkedList<Long> childrenIds,
                                               @Param("storageId") Long storageId,
                                               @Param("orderBy") String orderBy,
                                               @Param("isAsc") Boolean isAsc,
                                               @Param("type") Integer type);

    Long selectPageByStorageCount(@Param("title") String title,
                                  @Param("categoryId") Long categoryId,
                                  @Param("childrenIds") LinkedList<Long> childrenIds,
                                  @Param("storageId") Long storageId,
                                  @Param("orderBy") String orderBy,
                                  @Param("isAsc") Boolean isAsc);

    List<KxStoreProduct> getProductTitleAll();


    KxStoreProductVo getProductByIdAndStorageId(@Param("productId") Long productId, @Param("storageId") Long storageId);

    /**
     * 库存扣减
     * @param productId
     * @param num
     * @param storeId
     * @return
     */
    Integer decSkuStock(@Param("productId") Long productId, @Param("num") Integer num, @Param("storeId") Long storeId);

    /**
     * 库存回扣
     * @param productId
     * @param num
     * @param storeId
     * @return
     */
    Integer restoreSkuStock(@Param("productId") Long productId, @Param("num") Integer num, @Param("storeId") Long storeId);

    /**
     * 增加销量
     * @param productId
     * @param num
     */
    void incSales(@Param("productId") Long productId, @Param("num") Integer num);


    /**
     * 获取产品通过仓库
     * @param storageId
     * @return
     */
    List<KxStoreProduct> getProductTitleAllByStorageId(@Param("storageId") Long storageId);

    /**
     * 秒杀库存扣减
     * @param productId
     * @param cartNum
     * @param storeId
     * @return
     */
    Integer decSeckillStock(@Param("productId") Long productId, @Param("seckillId") Long seckillId, @Param("cartNum") Integer cartNum, @Param("storeId") Long storeId);

    /**
     * 查询该仓库下的多规格
     * @param storageId
     * @param commonId
     * @return
     */
    List<KxStoreProduct> selectListByStorage(@Param("storageId") Long storageId, @Param("commonId") String commonId);
}
