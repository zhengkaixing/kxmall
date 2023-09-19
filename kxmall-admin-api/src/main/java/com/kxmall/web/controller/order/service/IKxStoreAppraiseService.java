package com.kxmall.web.controller.order.service;

import com.kxmall.order.domain.vo.KxStoreAppraiseVo;
import com.kxmall.order.domain.bo.KxStoreAppraiseBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 评论管理Service接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface IKxStoreAppraiseService {

    /**
     * 查询评论管理
     */
    KxStoreAppraiseVo queryById(Long id);

    /**
     * 查询评论管理列表
     */
    TableDataInfo<KxStoreAppraiseVo> queryPageList(KxStoreAppraiseBo bo, PageQuery pageQuery);

    /**
     * 查询评论管理列表
     */
    List<KxStoreAppraiseVo> queryList(KxStoreAppraiseBo bo);

    /**
     * 新增评论管理
     */
    Boolean insertByBo(KxStoreAppraiseBo bo);

    /**
     * 修改评论管理
     */
    Boolean updateByBo(KxStoreAppraiseBo bo);

    /**
     * 校验并批量删除评论管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
