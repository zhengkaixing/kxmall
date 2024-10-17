package com.kxmall.web.controller.activity.service;

import com.kxmall.activity.domain.bo.KxStoreActivityProductBo;
import com.kxmall.activity.domain.vo.KxStoreActivityProductVo;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 活动商品Service接口
 *
 * @author kxmall
 * @date 2024-08-07
 */
public interface IKxStoreActivityProductService {

    /**
     * 查询活动商品
     */
    KxStoreActivityProductVo queryById(Long id);

    /**
     * 查询活动商品列表
     */
    TableDataInfo<KxStoreActivityProductVo> queryPageList(KxStoreActivityProductBo bo, PageQuery pageQuery);

    /**
     * 查询活动商品列表
     */
    List<KxStoreActivityProductVo> queryList(KxStoreActivityProductBo bo);

    /**
     * 新增活动商品
     */
    Boolean insertByBo(KxStoreActivityProductBo bo);

    /**
     * 修改活动商品
     */
    Boolean updateByBo(KxStoreActivityProductBo bo);

    /**
     * 校验并批量删除活动商品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Boolean addProductBatch(KxStoreActivityProductBo bo);

    Boolean updatePrice(KxStoreActivityProductBo bo);
}
