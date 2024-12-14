package com.kxmall.oss.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 文件对象 sys_oss_content
 *
 * @author kxmall
 * @date 2024-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oss_content")
public class SysOssContent extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 配置编号
     */
    private String configKey;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 文件内容
     */
    private byte[] content;
    /**
     * 是否删除
     */
    private Integer deleted;

}
