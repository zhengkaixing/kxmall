package com.kxmall.web.controller.system.service;

import com.kxmall.system.domain.bo.SysRegionBo;
import com.kxmall.system.domain.vo.SysRegionVo;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 中国地区系统Service接口
 *
 * @author kxmall
 * @date 2023-02-08
 */
public interface ISysRegionService {

    /**
     * 查询中国地区系统
     */
    SysRegionVo queryById(Long id);

    /**
     * 查询中国地区系统列表
     */
    TableDataInfo<SysRegionVo> queryPageList(SysRegionBo bo, PageQuery pageQuery);

    /**
     * 查询中国地区系统列表
     */
    List<SysRegionVo> queryList(SysRegionBo bo);

    /**
     * 新增中国地区系统
     */
    Boolean insertByBo(SysRegionBo bo);

    /**
     * 修改中国地区系统
     */
    Boolean updateByBo(SysRegionBo bo);

    /**
     * 校验并批量删除中国地区系统信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
