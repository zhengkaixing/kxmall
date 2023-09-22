package com.kxmall.user.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import com.kxmall.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 客户收藏业务对象 kx_user_collect
 *
 * @author kxmall
 * @date 2023-04-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxUserCollectBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long productId;


}
