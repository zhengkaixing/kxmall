package com.kxmall.decorate.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import com.kxmall.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


/**
 * 首页页面模板业务对象 kx_page_template
 *
 * @author kxmall
 * @date 2023-11-05
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxPageTemplateBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
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
