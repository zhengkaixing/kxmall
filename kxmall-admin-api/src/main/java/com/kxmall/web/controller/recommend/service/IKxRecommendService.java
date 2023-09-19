package com.kxmall.web.controller.recommend.service;

import com.kxmall.recommend.domain.vo.KxRecommendVo;
import com.kxmall.recommend.domain.bo.KxRecommendBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 推荐管理Service接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface IKxRecommendService {

    /**
     * 查询推荐管理
     */
    KxRecommendVo queryById(Long id);

    /**
     * 查询推荐管理列表
     */
    TableDataInfo<KxRecommendVo> queryPageList(KxRecommendBo bo, PageQuery pageQuery);

    /**
     * 查询推荐管理列表
     */
    List<KxRecommendVo> queryList(KxRecommendBo bo);

    /**
     * 新增推荐管理
     */
    Boolean insertByBo(KxRecommendBo bo);

    /**
     * 修改推荐管理
     */
    Boolean updateByBo(KxRecommendBo bo);

    /**
     * 校验并批量删除推荐管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    /**
     * 批量新增
     * @param boList
     * @return
     */
    Boolean addRecommendBatch(KxRecommendBo bo);
}
