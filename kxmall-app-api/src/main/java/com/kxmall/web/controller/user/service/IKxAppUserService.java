package com.kxmall.web.controller.user.service;

import com.kxmall.user.domain.bo.KxUserBo;
import com.kxmall.user.domain.vo.KxUserVo;

/**
 * 足迹Service接口
 *
 * @author kxmall
 * @date 2023-04-06
 */
public interface IKxAppUserService {

    /**
     * 查询用户
     */
    KxUserVo queryById(Long id);


    /**
     * 修改用户
     */
    Boolean updateByBo(KxUserBo bo);

}
