package com.kxmall.common.core.controller;

import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.domain.model.LoginUser;
import com.kxmall.common.enums.DeviceType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.helper.LoginHelper;
import com.kxmall.common.utils.StringUtils;

/**
 * web层通用数据处理
 *
 * @author 郅兴开源团队-小黑
 */
public class BaseController {

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected R<Void> toAjax(int rows) {
        return rows > 0 ? R.ok() : R.fail();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected R<Void> toAjax(boolean result) {
        return result ? R.ok() : R.fail();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser() {
        if (!DeviceType.PC.equals(LoginHelper.getDeviceType())) {
            throw new ServiceException("用户类型不正确");
        }
        return LoginHelper.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    public Long getUserId() {
        if (!DeviceType.PC.equals(LoginHelper.getDeviceType())) {
            throw new ServiceException("用户类型不正确");
        }
        return LoginHelper.getUserId();
    }

    /**
     * 获取登录部门id
     */
    public Long getDeptId() {
        if (!DeviceType.PC.equals(LoginHelper.getDeviceType())) {
            throw new ServiceException("用户类型不正确");
        }
        return LoginHelper.getDeptId();
    }

    /**
     * 获取登录用户名
     */
    public String getUsername() {
        if (!DeviceType.PC.equals(LoginHelper.getDeviceType())) {
            throw new ServiceException("用户类型不正确");
        }
        return LoginHelper.getUsername();
    }
}
