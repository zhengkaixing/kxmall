package com.kxmall.product.mapper;

import com.kxmall.product.domain.vo.KxStoreProductVo;
import com.kxmall.product.domain.KxStoreProduct;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;

/**
 * 商品Mapper接口
 *
 * @author kxmall
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
                                               @Param("isAsc") Boolean isAsc);

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
}
