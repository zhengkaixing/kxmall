package com.kxmall.common.filter;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.enums.DeviceType;
import com.kxmall.common.utils.ServletUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 演示 Filter，禁止用户发起写操作，避免影响测试数据
 *
 * @author xiaohei
 */
public class DemoFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        return !StrUtil.equalsAnyIgnoreCase(method, "POST", "PUT", "DELETE")  // 写操作时，不进行过滤率
                || request.getRequestURI().contains("cb/wxpay")
                || request.getRequestURI().contains("logout")
                || StpUtil.getTokenSession() == null
                || (StpUtil.getTokenSession().get("loginUser") != null && !DeviceType.PC.getDevice().equals(StpUtil.getLoginDevice())); // 非登录用户时，不进行过滤
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        // 直接返回 DEMO_DENY 的结果。即，请求不继续
        ServletUtils.writeJSON(response, R.fail(901, "演示模式，禁止写操作"));
    }

}
