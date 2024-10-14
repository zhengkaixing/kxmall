package com.kxmall.activity.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import com.kxmall.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


/**
 * 活动商品业务对象 kx_store_activity
 *
 * @author kxmall
 * @date 2024-08-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxStoreActivityBo extends BaseEntity {

    /**
     * 活动id
     */
    @NotNull(message = "活动id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动海报
     */
    private String imgUrl;

    /**
     * 海报链接
     */
    private String outUrl;

    /**
     * 活动详情
     */
    private String description;

    /**
     * 状态(是否开启)
     */
    private Integer status;


}
