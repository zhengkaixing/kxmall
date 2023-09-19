package com.kxmall.user.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 足迹对象 kx_user_footprint
 *
 * @author kxmall
 * @date 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_user_footprint")
public class KxUserFootprint extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
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
