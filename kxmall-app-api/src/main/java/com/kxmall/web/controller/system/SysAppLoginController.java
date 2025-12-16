package com.kxmall.web.controller.system;

import cn.dev33.satoken.annotation.SaIgnore;
import com.kxmall.common.constant.Constants;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.domain.model.LoginUser;
import com.kxmall.common.enums.UserLoginType;
import com.kxmall.user.domain.vo.KxUserVo;
import com.kxmall.web.controller.system.service.ISysAppLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * APP端登录认证控制器
 * 提供多种登录方式：账号密码登录、手机验证码登录、小程序登录、H5登录等
 *
 * @author 郅兴开源团队-小黑
 * @version 1.0
 * @date 2023/9/1
 */
@Validated
@RequiredArgsConstructor
@RestController
public class SysAppLoginController extends BaseAppController {

    /**
     * 登录服务接口
     */
    private final ISysAppLoginService appLoginService;

    /**
     * 账号密码登录
     * 使用用户名和密码进行登录，登录成功后返回token
     *
     * @param username 用户名
     * @param password 密码（建议前端加密后传输）
     * @return R<Map<String, Object>> 包含token的响应对象
     */
    @SaIgnore
    @GetMapping("/accountLogin")
    public R<Map<String, Object>> accountLogin(String username, String password) {
        Map<String, Object> ajax = new HashMap<>();
        // 调用登录服务，生成并返回token
        String token = appLoginService.accountLogin(username, password);
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);
    }

    /**
     * 手机验证码登录
     * 使用手机号和验证码进行登录，登录成功后返回用户信息（包含token）
     *
     * @param phone 手机号
     * @param verifyCode 验证码
     * @return R<KxUserVo> 用户信息响应对象（包含token）
     */
    @SaIgnore
    @GetMapping("/phoneLogin")
    public R<KxUserVo> phoneLogin(String phone, String verifyCode) {
        // 调用登录服务，返回用户信息（包含token）
        KxUserVo userVo = appLoginService.phoneLogin(phone, verifyCode);
        return R.ok(userVo);
    }

    /**
     * 账号密码注册
     * 使用用户名和密码进行注册，注册成功后自动登录并返回token
     *
     * @param username 用户名
     * @param password 密码（建议前端加密后传输）
     * @return R<Map<String, Object>> 包含token的响应对象
     */
    @SaIgnore
    @GetMapping("/accountRegister")
    public R<Map<String, Object>> accountRegister(String username, String password) {
        Map<String, Object> ajax = new HashMap<>();
        // 调用注册服务，注册成功后自动登录并返回token
        String token = appLoginService.accountRegister(username, password);
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);
    }

    /**
     * 修改账号密码
     * 需要先登录，然后使用旧密码验证身份后修改为新密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return R<Boolean> 修改结果，true表示成功，false表示失败
     */
    @GetMapping("/accountUpdate")
    public R<Boolean> accountUpdate(String oldPassword, String newPassword) {
        // 从token中获取当前登录用户信息
        LoginUser loginUser = getAppLoginUser();
        // 调用服务更新密码
        return R.ok(appLoginService.accountUpdate(loginUser.getUsername(), oldPassword, newPassword));
    }

    /**
     * H5公众号登录
     * 通过微信H5授权获取code，然后换取用户信息并登录
     *
     * @param code 微信授权code（必填）
     * @return R<KxUserVo> 用户信息响应对象（包含token）
     */
    @SaIgnore
    @GetMapping("/h5Login")
    public R<KxUserVo> h5Login(@NotBlank(message = "{h5.code.not.blank}") String code) {
        // 调用H5登录服务
        return R.ok(appLoginService.h5Login(code));
    }

    /**
     * 小程序登录（第一步）
     * 通过微信小程序code获取openid和session_key
     *
     * @param loginType 登录类型（微信小程序等）
     * @param code 小程序授权code
     * @return R<KxUserVo> 用户信息响应对象（包含token），如果登录类型不匹配则返回null
     */
    @SaIgnore
    @GetMapping("/miniLogin")
    public R<KxUserVo> miniLogin(Integer loginType, String code) {
        // 判断是否为微信小程序登录
        if (loginType.equals(UserLoginType.MP_WEIXIN.getCode())) {
            return R.ok(appLoginService.miniLogin(code));
        }
        return null;
    }

    /**
     * 小程序登录（第二步）
     * 解密手机号等信息，完成用户信息绑定和登录
     *
     * @param encryptedData 加密的用户数据
     * @param iv 初始向量
     * @param loginType 登录类型
     * @param session_key 会话密钥
     * @param openId 用户openId
     * @param avatar 用户头像URL
     * @param nickName 用户昵称
     * @return R<KxUserVo> 用户信息响应对象（包含token）
     */
    @SaIgnore
    @GetMapping("/authPhone")
    public R<KxUserVo> authPhone(String encryptedData, String iv, Integer loginType, 
                                 String session_key, String openId, String avatar, String nickName) {
        // 调用服务解密手机号等信息并完成登录
        return R.ok(appLoginService.authPhone(encryptedData, iv, loginType, session_key, openId, avatar, nickName));
    }

}
