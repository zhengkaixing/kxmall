package com.kxmall.web.controller.user.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.user.domain.bo.KxUserBo;
import com.kxmall.user.domain.vo.KxPromUserVo;
import com.kxmall.user.domain.vo.KxUserVo;

import java.util.Collection;
import java.util.List;

/**
 * 用户Service接口
 *
 * @author kxmall
 * @date 2023-02-14
 */
public interface IKxUserService {

    /**
     * 查询用户
     */
    KxUserVo queryById(Long uid);

    /**
     * 查询用户列表
     */
    TableDataInfo<KxUserVo> queryPageList(KxUserBo bo, PageQuery pageQuery);

    /**
     * 查询用户列表
     */
    List<KxUserVo> queryList(KxUserBo bo);

    /**
     * 新增用户
     */
    Boolean insertByBo(KxUserBo bo);

    /**
     * 修改用户
     */
    Boolean updateByBo(KxUserBo bo);

    /**
     * 校验并批量删除用户信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查看下级
     *
     * @return
     */
    List<KxPromUserVo> querySpread(KxUserBo bo);


    /**
     * 获取我的分销下人员列表
     *
     * @param uid     uid
     * @param page    page
     * @param limit   limit
     * @param grade   ShopCommonEnum.GRADE_0
     * @param keyword 关键字搜索
     * @param sort    排序
     * @return list
     */
    List<KxPromUserVo> getUserSpreadGrade(Long uid, int page, int limit, Integer grade, String keyword, String sort);

    /**
     * 更改状态
     */
    void onStatus(Long id, Integer status);

    /**
     * 修改余额
     */
    void updateMoney(KxUserBo param);

    /**
     * 通过uid获取用户信息
     *
     * @param uid
     * @return
     */
    KxUserVo selectByUid(Long uid);
}
