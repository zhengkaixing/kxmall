package com.kxmall.recommend.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 推荐管理对象 kx_recommend
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_recommend")
public class KxRecommend extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 商品id
     */
    private Long productId;
    /**
     * 推荐类型1特价推荐
     */
    private Long recommendType;

}
