package com.kxmall.wechat.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Schema(description = "RPC 服务 - 微信小程序订阅消息发送 Request DTO")
@Data
public class SocialWxaSubscribeMessageSendReqVo {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "用户编号不能为空")
    private Long userId;
    @Schema(description = "用户类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "用户类型不能为空")
    private Integer userType;

    @Schema(description = "消息模版标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "模版标题")
    @NotEmpty(message = "消息模版标题不能为空")
    private String templateTitle;

    @Schema(description = "点击模板卡片后的跳转页面，仅限本小程序内的页面", example = "pages/index?foo=bar")
    private String page; // 支持带参数，（示例 index?foo=bar ）。该字段不填则模板无跳转。

    @Schema(description = "模板内容的参数")
    private Map<String, String> messages;

    public SocialWxaSubscribeMessageSendReqVo addMessage(String key, String value) {
        if (messages == null) {
            messages = new HashMap<>();
        }
        messages.put(key, value);
        return this;
    }

    /**
     * 设置用户ID
     * @param userId 用户ID
     * @return 当前对象，支持链式调用
     */
    public SocialWxaSubscribeMessageSendReqVo setUserId(Long userId) {
        this.userId = userId;
        return this; // 返回当前对象，支持链式调用
    }

    /**
     * 设置模板标题
     * @param templateTitle 模板标题
     * @return 当前对象，支持链式调用
     */
    public SocialWxaSubscribeMessageSendReqVo setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
        return this; // 返回当前对象，支持链式调用
    }

    /**
     * 设置页面路径
     * @param page 小程序页面路径
     * @return 当前对象，支持链式调用
     */
    public SocialWxaSubscribeMessageSendReqVo setPage(String page) {
        this.page = page;
        return this; // 返回当前对象，支持链式调用
    }

}
