package com.kxmall.web.controller.user.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.user.domain.bo.KxUserLevelSettingBo;
import com.kxmall.user.domain.vo.KxUserLevelSettingVo;

import java.util.Collection;
import java.util.List;

/**
 * 设置用户等级Service接口
 *
 * @author kxmall
 * @date 2023-02-21
 */
public interface IKxUserLevelSettingService {

    /**
     * 查询设置用户等级
     */
    KxUserLevelSettingVo queryById(Long id);

    /**
     * 查询设置用户等级列表
     */
    TableDataInfo<KxUserLevelSettingVo> queryPageList(KxUserLevelSettingBo bo, PageQuery pageQuery);

    /**
     * 查询设置用户等级列表
     */
    List<KxUserLevelSettingVo> queryList(KxUserLevelSettingBo bo);

    /**
     * 新增设置用户等级
     */
    Boolean insertByBo(KxUserLevelSettingBo bo);

    /**
     * 修改设置用户等级
     */
    Boolean updateByBo(KxUserLevelSettingBo bo);

    /**
     * 校验并批量删除设置用户等级信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
