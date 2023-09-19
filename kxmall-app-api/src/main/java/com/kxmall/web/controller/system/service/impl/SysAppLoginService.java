package com.kxmall.web.controller.system.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.kxmall.common.constant.CacheConstants;
import com.kxmall.common.core.domain.model.LoginUser;
import com.kxmall.common.enums.DeviceType;
import com.kxmall.common.enums.UserLoginType;
import com.kxmall.common.enums.UserType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.helper.LoginHelper;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.user.domain.KxUser;
import com.kxmall.user.domain.vo.KxUserVo;
import com.kxmall.user.mapper.KxUserMapper;
import com.kxmall.web.controller.system.service.ISysAppLoginService;
import com.kxmall.wechat.WxMaConfiguration;
import com.kxmall.wechat.WxMpConfiguration;
import com.squareup.okhttp.OkHttpClient;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/2
 */
@RequiredArgsConstructor
@Service
public class SysAppLoginService implements ISysAppLoginService {


    private final KxUserMapper kxUserMapper;

    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public KxUserVo miniLogin(String code) {
        try {
            WxMaService wxMaService = WxMaConfiguration.getWxMaService();
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);

            List<KxUser> userList = kxUserMapper.selectList(new LambdaQueryWrapper<KxUser>().eq(KxUser::getOpenId, session.getOpenid()));
            if (!CollectionUtils.isEmpty(userList)) {
                //若用户已经注册，则直接返回用户
                KxUserVo kxUserVo = new KxUserVo();
                BeanUtils.copyProperties(userList.get(0), kxUserVo);
                // 此处可根据登录用户的数据不同 自行创建 loginUser
                LoginUser loginUser = buildLoginUser(userList.get(0));
                // 生成token
                LoginHelper.loginByDevice(loginUser, DeviceType.APP);
                kxUserVo.setAccessToken(StpUtil.getTokenValue());
                return kxUserVo;
            } else {
                //若用户为空，则注册此用户
                KxUserVo userVo = new KxUserVo();
                userVo.setOpenId(session.getOpenid());
                userVo.setLoginType(UserLoginType.MP_WEIXIN.getCode());
                userVo.setSessionKey(session.getSessionKey());
                return userVo;
//                String userAccessToken = wxMaService.getAccessToken(true);
//                //通过用户AccessToken换取用户信息
//                String userInfoJson;
//                try {
//                    userInfoJson = okHttpClient.newCall(
//                        new Request.Builder().url("https://api.weixin.qq.com/sns/userinfo?access_token="
//                            + userAccessToken + "&openid=" + session.getOpenid() + "&lang=zh_CN").build()).execute().body().string();
//                } catch (IOException e) {
//                    throw new ServiceException(e.getMessage());
//                }
//                JSONObject userInfoJsonObject = JSONObject.parseObject(userInfoJson);
//
//                Date now = new Date();
//                KxUser newUserDO = new KxUser();
//                newUserDO.setLoginType(UserLoginType.MP_WEIXIN.getCode());
//                newUserDO.setNickname(userInfoJsonObject.getString("nickname"));
//                newUserDO.setAvatar(userInfoJsonObject.getString("headimgurl"));
//                newUserDO.setOpenId(session.getOpenid());
//                newUserDO.setUpdateTime(now);
//                newUserDO.setCreateTime(now);
//                kxUserMapper.insert(newUserDO);
//                //这一步是为了封装上数据库上配置的默认值
//                KxUser user = kxUserMapper.selectById(newUserDO.getUid());
//
//                //若用户已经注册，则直接返回用户
//                KxUserVo kxUserVo = new KxUserVo();
//                BeanUtils.copyProperties(user, kxUserVo);
//                // 此处可根据登录用户的数据不同 自行创建 loginUser
//                LoginUser loginUser = buildLoginUser(userList.get(0));
//                // 生成token
//                LoginHelper.loginByDevice(loginUser, DeviceType.APP);
//                kxUserVo.setAccessToken(StpUtil.getTokenValue());
//                return kxUserVo;
            }
        } catch (WxErrorException e) {
            throw new ServiceException(e.toString());
        }
    }

    @Override
    public String h5Login(String code) {
        try {
            WxMpService wxService = WxMpConfiguration.getWxMpService();
            WxOAuth2AccessToken wxMpOAuth2AccessToken = wxService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo wxMpUser = wxService.getOAuth2Service().getUserInfo(wxMpOAuth2AccessToken, null);
            String openid = wxMpUser.getOpenid();
            KxUser user = kxUserMapper.selectOne(new LambdaQueryWrapper<KxUser>()
                .eq(KxUser::getGzhOpenId, openid));

            if (user == null) {
                user = new KxUser();
                user.setUsername(openid);
                user.setNickname(wxMpUser.getNickname());
                user.setAvatar(wxMpUser.getHeadImgUrl());
                user.setUserType(UserType.APP_USER.getUserType());
                user.setGzhOpenId(openid);
                kxUserMapper.insert(user);
            }
            // 此处可根据登录用户的数据不同 自行创建 loginUser
            LoginUser loginUser = buildLoginUser(user);
            // 生成token
            LoginHelper.loginByDevice(loginUser, DeviceType.APP);
            return StpUtil.getTokenValue();
        } catch (WxErrorException e) {
            throw new ServiceException("微信授权失败！");
        }
    }

    @Override
    public String accountLogin(String username, String password) {
        KxUser user = kxUserMapper.selectOne(new LambdaQueryWrapper<KxUser>()
            .eq(KxUser::getUsername, username)
            .eq(KxUser::getPassword, SecureUtil.md5(password)));
        if (ObjectUtils.isEmpty(user)) {
            throw new ServiceException("账号或者密码不正确");
        }
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.APP);
        return StpUtil.getTokenValue();
    }

    @Override
    public String accountRegister(String username, String password) {
        KxUser user = kxUserMapper.selectOne(new LambdaQueryWrapper<KxUser>()
            .eq(KxUser::getUsername, username));
        if (ObjectUtils.isNotEmpty(user)) {
            throw new ServiceException("用户账号已存在！");
        }
        user = new KxUser();
        user.setNickname(username);
        user.setUserType("account");
        user.setPassword(SecureUtil.md5(password));
        kxUserMapper.insert(user);
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.APP);
        return StpUtil.getTokenValue();
    }

    @Override
    public Boolean accountUpdate(String username, String oldPassword, String newPassword) {
        KxUser user = kxUserMapper.selectOne(new LambdaQueryWrapper<KxUser>()
            .eq(KxUser::getUsername, username)
            .eq(KxUser::getPassword, SecureUtil.md5(oldPassword)));
        if (ObjectUtils.isNotEmpty(user)) {
            throw new ServiceException("旧密码不正确！");
        }
        KxUser update = new KxUser();
        update.setUid(user.getUid());
        update.setPassword(SecureUtil.md5(newPassword));
        kxUserMapper.updateById(user);
        StpUtil.logout();
        return true;
    }

    @Override
    public KxUserVo phoneLogin(String phone, String verifyCode) {
        //1.校验验证码
        checkVerifyCode(phone, verifyCode);
        //2.校验用户是否存在
        KxUser userdo;
        Date now = new Date();
        List<KxUser> KxUserByPhoneList = kxUserMapper.selectList(new QueryWrapper<KxUser>().eq("phone", phone));
        if (KxUserByPhoneList == null || KxUserByPhoneList.size() == 0) {
            // 注册
            KxUser newKxUser = new KxUser();
            newKxUser.setLoginType(UserLoginType.REGISTER.getCode());
            newKxUser.setPhone(phone);
            newKxUser.setUpdateTime(now);
            newKxUser.setUserType(UserType.APP_USER.getUserType());
            newKxUser.setCreateTime(now);
            kxUserMapper.insert(newKxUser);
            //这一步是为了封装上数据库上配置的默认值
            userdo = kxUserMapper.selectById(newKxUser.getUid());
        } else {
            //登录
            userdo = KxUserByPhoneList.get(0);
            if (userdo.getStatus() == 0) {
                throw new ServiceException("用户处于冻结状态，请联系管理员");
            }
            KxUser userUpdateDO = new KxUser();
            userUpdateDO.setUid(userdo.getUid());
            userUpdateDO.setUpdateTime(now);
            kxUserMapper.updateById(userUpdateDO);
        }

        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(userdo);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.APP);
        KxUserVo userVo = new KxUserVo();
        BeanUtils.copyProperties(userdo, userVo);
        userVo.setAccessToken(StpUtil.getTokenValue());
        return userVo;
    }

    @Override
    public KxUserVo authPhone(String encryptedData,
                              String iv,
                              Integer loginType,
                              String session_key,
                              String openId,
                              String avatarUrl,
                              String nickName) {

        WxMaService wxMaService = WxMaConfiguration.getWxMaService();
        WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService()
            .getPhoneNoInfo(session_key, encryptedData, iv);

        KxUser kxUser = kxUserMapper.selectOne(new LambdaQueryWrapper<KxUser>().eq(KxUser::getOpenId, openId));

        if (ObjectUtils.isEmpty(kxUser)) {
            Date now = new Date();
            KxUser newUserDO = new KxUser();
            newUserDO.setLoginType(UserLoginType.MP_WEIXIN.getCode());
            newUserDO.setNickname(nickName);
            newUserDO.setPhone(phoneNoInfo.getPhoneNumber());
            newUserDO.setAvatar(avatarUrl);
            newUserDO.setOpenId(openId);
            newUserDO.setUpdateTime(now);
            newUserDO.setCreateTime(now);
            newUserDO.setUserType(UserType.APP_USER.getUserType());
            kxUserMapper.insert(newUserDO);
            //这一步是为了封装上数据库上配置的默认值
            kxUser = kxUserMapper.selectById(newUserDO.getUid());
        }
        //若用户已经注册，则直接返回用户
        KxUserVo kxUserVo = new KxUserVo();
        BeanUtils.copyProperties(kxUser, kxUserVo);
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(kxUser);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.APP);
        kxUserVo.setAccessToken(StpUtil.getTokenValue());
        return kxUserVo;
    }

    private void checkVerifyCode(String phone, String verifyCode) {
        String raw = RedisUtils.getCacheObject(CacheConstants.CAPTCHA_CODE_KEY + phone);
        if (StringUtils.isEmpty(raw)) {
            throw new ServiceException("验证码未发送或已过期");
        }
        if (!raw.equals(verifyCode)) {
            throw new ServiceException("验证码不正确");
        }


    }

    private LoginUser buildLoginUser(KxUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUid());
        loginUser.setUsername(user.getUsername());
        loginUser.setUserType(UserType.APP_USER.getUserType());
        if (user.getLoginType().equals(UserLoginType.MP_WEIXIN.getCode())) {
            loginUser.setOpenId(user.getOpenId());
        } else if (user.getLoginType().equals(UserLoginType.PHONE_WEIXIN.getCode())) {
            loginUser.setOpenId(user.getOpenId());
        } else if (user.getLoginType().equals(UserLoginType.H5_WEIXIN.getCode())) {
            loginUser.setOpenId(user.getGzhOpenId());
        } else {
            loginUser.setOpenId(user.getOpenId());
        }
        loginUser.setLoginType(user.getLoginType());
        return loginUser;
    }
}
