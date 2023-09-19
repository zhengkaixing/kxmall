package com.kxmall.web.controller.system.service;

import com.kxmall.user.domain.vo.KxUserVo;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/2
 */
public interface ISysAppLoginService {

    /**
     * 小程序登录
     * @param code
     * @return
     */
    KxUserVo miniLogin(String code);

    /**
     * h5登录
     * @param openid
     * @return
     */
    String h5Login(String openid);

    /**
     * 账号密码登录
     * @param username
     * @param password
     * @return
     */
    String accountLogin(String username, String password);

    /**
     * 账号密码注册
     * @param username
     * @param password
     * @return
     */
    String accountRegister(String username, String password);

    /**
     * 密码修改
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return
     */
    Boolean accountUpdate(String username, String oldPassword, String newPassword);

    /**
     * 手机验证码登录
     * @param phone
     * @param verifyCode
     * @return
     */
    KxUserVo phoneLogin(String phone, String verifyCode);

    /**
     * 登录
     * @param encryptedData
     * @param iv
     * @param loginType
     * @param session_key
     * @param openId
     * @param avatarUrl
     * @param nickName
     * @return
     */
    KxUserVo authPhone(String encryptedData,
                       String iv,
                       Integer loginType,
                       String session_key,
                       String openId,
                       String avatarUrl,
                       String nickName);
}
