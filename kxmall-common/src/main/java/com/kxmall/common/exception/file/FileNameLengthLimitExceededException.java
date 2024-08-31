package com.kxmall.common.exception.file;

/**
 * 文件名称超长限制异常类
 *
 * @author 郅兴开源团队-小黑
 */
public class FileNameLengthLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength});
    }
}
