package com.kxmall.decorate.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 首页页面模板对象 kx_page_template
 *
 * @author kxmall
 * @date 2023-11-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_page_template")
public class KxPageTemplate extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 0停用1启用
     */
    private Integer status;
    /**
     * 首页json格式
     */
    private String content;

}
