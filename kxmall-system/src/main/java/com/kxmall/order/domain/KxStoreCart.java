package com.kxmall.order.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 购物车对象 kx_store_cart
 *
 * @author kxmall
 * @date 2023-02-15
 */
@Data
@TableName("kx_store_cart")
public class KxStoreCart {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户id
     */
    private Long uid;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 购买东西的详细信息
     */
    private Long cartNum;


    private Date createTime;


    private Date updateTime;
}
