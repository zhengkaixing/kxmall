package com.kxmall.web.controller.user.service;

import com.kxmall.user.domain.vo.KxUserLevelVo;
import com.kxmall.user.domain.bo.KxUserLevelBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户等级Service接口
 *
 * @author kxmall
 * @date 2023-02-14
 */
public interface IKxUserLevelService {

    /**
     * 查询用户等级
     */
    KxUserLevelVo queryById(Long id);

    /**
     * 查询用户等级列表
     */
    TableDataInfo<KxUserLevelVo> queryPageList(KxUserLevelBo bo, PageQuery pageQuery);

    /**
     * 查询用户等级列表
     */
    List<KxUserLevelVo> queryList(KxUserLevelBo bo);

    /**
     * 新增用户等级
     */
    Boolean insertByBo(KxUserLevelBo bo);

    /**
     * 修改用户等级
     */
    Boolean updateByBo(KxUserLevelBo bo);

    /**
     * 校验并批量删除用户等级信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
