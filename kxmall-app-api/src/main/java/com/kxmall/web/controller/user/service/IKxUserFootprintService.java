package com.kxmall.web.controller.user.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.user.domain.bo.KxUserFootprintBo;
import com.kxmall.user.domain.vo.KxUserFootprintVo;

import java.util.Collection;
import java.util.List;

/**
 * 足迹Service接口
 *
 * @author kxmall
 * @date 2023-04-06
 */
public interface IKxUserFootprintService {

    /**
     * 查询足迹
     */
    KxUserFootprintVo queryById(Long id);

    /**
     * 查询足迹列表
     */
    TableDataInfo<KxUserFootprintVo> queryPageList(KxUserFootprintBo bo, PageQuery pageQuery);

    /**
     * 查询足迹列表
     */
    List<KxUserFootprintVo> queryList(KxUserFootprintBo bo);

    /**
     * 新增足迹
     */
    Boolean insertByBo(KxUserFootprintBo bo);

    /**
     * 修改足迹
     */
    Boolean updateByBo(KxUserFootprintBo bo);

    /**
     * 校验并批量删除足迹信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 处理足迹
     *
     * @param userId
     * @param producId
     */
    boolean addOrUpdateFootprint(Long userId, Long producId);
}
