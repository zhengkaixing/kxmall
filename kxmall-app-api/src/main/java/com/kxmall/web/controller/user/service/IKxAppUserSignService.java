package com.kxmall.web.controller.user.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.user.domain.bo.KxUserSignBo;
import com.kxmall.user.domain.vo.KxUserSignVo;
import com.kxmall.user.domain.vo.KxUserVo;

import java.util.Collection;
import java.util.List;

/**
 * 签到记录Service接口
 *
 * @author kxmall
 * @date 2024-08-26
 */
public interface IKxAppUserSignService {

    /**
     * 查询签到记录
     */
    KxUserSignVo queryById(Long id);

    /**
     * 查询签到记录列表
     */
    TableDataInfo<KxUserSignVo> queryPageList(KxUserSignBo bo, PageQuery pageQuery);

    /**
     * 查询签到记录列表
     */
    List<KxUserSignVo> queryList(KxUserSignBo bo);

    /**
     * 新增签到记录
     */
    Boolean insertByBo(KxUserSignBo bo);

    /**
     * 修改签到记录
     */
    Boolean updateByBo(KxUserSignBo bo);

    /**
     * 校验并批量删除签到记录信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 签到
     * @param userId
     * @return
     */
    Long sign(Long userId);


    /**
     * 获取签到的用户信息
     * @param userId
     * @return
     */
    KxUserVo getSignInUser(Long userId);
}
