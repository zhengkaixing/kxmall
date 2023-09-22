package com.kxmall.order.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 评论管理对象 kx_store_appraise
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_appraise")
public class KxStoreAppraise extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 商品id
     */
    private Long productId;
    /**
     * 规格id
     */
    private Long productAttrId;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 打分
     */
    private Long score;
    /**
     * 1表示已通过
     */
    private Long state;

}
