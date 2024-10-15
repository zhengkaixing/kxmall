package com.kxmall.web.controller;

import ch.qos.logback.core.OutputStreamAppender;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 企业微信日志添加器
 */
public class WeComAppender<E> extends OutputStreamAppender<E> {

    private String webhookUrl;
    private String msgFormat;
    private String application;
    private String instance;
    private Boolean enabled;

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void setMsgFormat(String msgFormat) {
        this.msgFormat = msgFormat;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void start() {
        WeComOutputStream weComOutputStream = new WeComOutputStream();
        weComOutputStream.setWebhookUrl(this.webhookUrl);
        weComOutputStream.setFormat(this.msgFormat);
        weComOutputStream.setApplication(this.application);
        weComOutputStream.setInstance(this.instance);
//        OutputStream targetStream = target.getStream();
//        // enable jansi only on Windows and only if withJansi set to true
//        if (EnvUtil.isWindows() && withJansi) {
//            targetStream = getTargetStreamForWindows(targetStream);
//        }
        String enabled = System.getenv("LOGBACK_WECOM_ENABLED");
        if (StringUtils.hasText(enabled)) {
            try {
                this.enabled = Boolean.valueOf(enabled);
            } catch (Exception e) {
                //
            }
        }
        if (Boolean.FALSE.equals(this.enabled)) {
            setOutputStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    //空输出
                }
            });
        } else {
            setOutputStream(weComOutputStream);
        }
        super.start();
    }

}
