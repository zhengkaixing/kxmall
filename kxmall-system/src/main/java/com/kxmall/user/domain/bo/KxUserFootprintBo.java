package com.kxmall.user.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 足迹业务对象 kx_user_footprint
 *
 * @author kxmall
 * @date 2023-04-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxUserFootprintBo extends BaseEntity {

    /**
     *
     */
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
