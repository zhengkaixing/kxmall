package com.kxmall.oss.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 上传返回体
 *
 * @author 郅兴开源团队-小黑
 */
@Data
@Builder
public class UploadResult {

    /**
     * 文件路径
     */
    private String url;

    /**
     * 文件名
     */
    private String filename;
}
