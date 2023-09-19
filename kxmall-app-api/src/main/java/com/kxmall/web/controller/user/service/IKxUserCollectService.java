package com.kxmall.web.controller.user.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.user.domain.bo.KxUserCollectBo;
import com.kxmall.user.domain.vo.KxUserCollectVo;

import java.util.Collection;
import java.util.List;

/**
 * 客户收藏Service接口
 *
 * @author kxmall
 * @date 2023-04-06
 */
public interface IKxUserCollectService {

    /**
     * 查询客户收藏
     */
    KxUserCollectVo queryById(Long id);

    /**
     * 查询客户收藏列表
     */
    TableDataInfo<KxUserCollectVo> queryPageList(KxUserCollectBo bo, PageQuery pageQuery);

    /**
     * 查询客户收藏列表
     */
    List<KxUserCollectVo> queryList(KxUserCollectBo bo);

    /**
     * 新增客户收藏
     */
    Boolean insertByBo(KxUserCollectBo bo);

    /**
     * 修改客户收藏
     */
    Boolean updateByBo(KxUserCollectBo bo);

    /**
     * 校验并批量删除客户收藏信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 删除客户收藏
     * @param bo
     * @return
     */
    int deleteCollect(KxUserCollectBo bo);

    /**
     * 查询某一用户收藏记录
     * @param bo
     * @param pageQuery
     * @return
     */
    TableDataInfo<KxUserCollectVo> getCollectAll(KxUserCollectBo bo, PageQuery pageQuery);
}
