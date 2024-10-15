package com.kxmall.web.controller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class WeComOutputStream extends OutputStream {

    private String webhookUrl;
    private String format;
    private String application;
    private String instance;
    private final RestTemplate restTemplate;
    private final LinkedBlockingQueue<byte[]> queue;
    private final ExecutorService executorService;
    private final AtomicBoolean isClose;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public WeComOutputStream() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(3000);
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(2000);
        this.restTemplate = new RestTemplate(factory);

        this.format = "{\n" +
                "    \"msgtype\":\"markdown\",\n" +
                "    \"markdown\":{\n" +
                "        \"content\":\"\n" +
                "<font color=\\\"red\\\">**警报发生**</font> \n" +
                "**主机地址:** {{instance}}\n" +
                "**应用名称:** {{application}}\n" +
                "**告警时间:** {{time}}\n" +
                "**告警摘要:** {{msg}}" +
                "        \"" +
                "    }\n" +
                "}}";

        this.queue = new LinkedBlockingQueue<>(1000);
        this.executorService = Executors.newSingleThreadExecutor();
        this.executorService.submit(this::sendMsg);
        this.isClose = new AtomicBoolean(false);
    }

    @Override
    public void write(int b) throws IOException {
        // 不需要处理
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (b == null || b.length <= 0) {
            return;
        }

        try {
            if (queue.size() > 1000) {
                return;
            }
            queue.put(b);
        } catch (InterruptedException e) {
            //
        }
    }

    private void sendMsg() {
        while (!isClose.get()) {
            try {
                byte[] msg = queue.poll(3, TimeUnit.SECONDS);
                if (msg == null) {
                    continue;
                }
                String payload = format;
                payload = StringUtils.replace(payload, "{{time}}", sdf.format(System.currentTimeMillis()));
                payload = StringUtils.replace(payload, "{{instance}}", this.instance());
                payload = StringUtils.replace(payload, "{{application}}", this.application());
                String msgStr = new String(msg, StandardCharsets.UTF_8);
                payload = StringUtils.replace(payload, "{{msg}}",  StringEscapeUtils.escapeJson(msgStr.substring(0,Math.min(350,msgStr.length()))));
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json;charset=utf-8");
                HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
                restTemplate.exchange(webhookUrl, HttpMethod.POST, requestEntity, Void.class);
            } catch (Exception e) {
                System.out.println("企业微信日志处理失败： "+e.getMessage());
                //不处理
            }
        }
    }

    private String application(){
        if (StringUtils.hasText(this.application)) {
            return this.application;
        }
        String application = System.getenv("spring.application.name");
        if (StringUtils.hasText(application)) {
            this.application = application;
            return this.application;
        }
        application = System.getenv("APPLICATION");
        if (StringUtils.hasText(application)) {
            this.application = application;
            return this.application;
        }
        this.application = "unknown";
        return this.application;
    }
    
    private String instance() {
        if (StringUtils.hasText(this.instance)) {
            return this.instance;
        }
        String instance = System.getenv("instance");
        if (StringUtils.hasText(instance)) {
            this.instance = instance;
            return this.instance;
        }
        instance = System.getenv("INSTANCE");
        if (StringUtils.hasText(instance)) {
            this.instance = instance;
            return this.instance;
        }

        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            if (StringUtils.hasText(hostAddress)) {
                this.instance = hostAddress;
                return this.instance;
            }
        } catch (UnknownHostException e) {
            //不处理
        }
        this.instance = "unknown";
        return this.instance;
    }

    @Override
    public void close() throws IOException {
//        super.close();
        this.isClose.set(true);
        this.executorService.shutdown();
        this.queue.clear();
    }


    @Override
    public void flush() throws IOException {
//        super.flush();
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void setFormat(String format) {
        if (format == null || format.trim().equals("")) {
            return;
        }
        this.format = format;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

}
