package com.kxmall.web.controller.activity.service;

import com.kxmall.activity.domain.bo.KxStoreActivityBo;
import com.kxmall.activity.domain.vo.KxStoreActivityVo;
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
public interface IKxStoreActivityService {

    /**
     * 查询活动商品
     */
    KxStoreActivityVo queryById(Long id);

    /**
     * 查询活动商品列表
     */
    TableDataInfo<KxStoreActivityVo> queryPageList(KxStoreActivityBo bo, PageQuery pageQuery);

    /**
     * 查询活动商品列表
     */
    List<KxStoreActivityVo> queryList(KxStoreActivityBo bo);

    /**
     * 新增活动商品
     */
    Boolean insertByBo(KxStoreActivityBo bo);

    /**
     * 修改活动商品
     */
    Boolean updateByBo(KxStoreActivityBo bo);

    /**
     * 校验并批量删除活动商品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
