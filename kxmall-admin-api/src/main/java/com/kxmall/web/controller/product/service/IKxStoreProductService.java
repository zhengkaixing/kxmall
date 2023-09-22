package com.kxmall.web.controller.product.service;

import com.kxmall.product.domain.bo.KxStoreProductBo;
import com.kxmall.product.domain.vo.KxStoreProductVo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.product.domain.vo.ProductTreeNodeVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品Service接口
 *
 * @author kxmall
 * @date 2023-02-13
 */
public interface IKxStoreProductService {

    /**
     * 查询商品
     */
    Map<String,Object> queryById(Long id);

    /**
     * 查询商品列表
     */
    TableDataInfo<KxStoreProductVo> queryPageList(KxStoreProductBo bo, PageQuery pageQuery);

    /**
     * 查询商品列表
     */
    List<KxStoreProductVo> queryList(KxStoreProductBo bo);

    /**
     * 新增商品
     */
    Boolean insertAndupdateByBo(KxStoreProductBo bo);

    /**
     * 修改商品
     */
    Boolean updateByBo(KxStoreProductBo bo);

    /**
     * 校验并批量删除商品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Long selectCountByCateId(Long CateId);

    /**
     * 商品上下架
     * @param id
     * @param status
     */
    void onSale(Long id, Integer status);

    Map<String, Object> getFormatAttr(Long id, String jsonStr, boolean isActivity);

    /**
     * 查询数量
     * @param productId
     * @return
     */
    Long selectCountById(Long productId);

    /**
     * 授权
     * @param bo
     * @return
     */
    Boolean batchAuthorizeGoods(KxStoreProductBo bo);

    /**
     * 获取产品树
     * @return
     */
    List<ProductTreeNodeVo> getProductBigTree();

}
