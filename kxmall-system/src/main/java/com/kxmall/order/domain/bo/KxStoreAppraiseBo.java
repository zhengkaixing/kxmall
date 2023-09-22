package com.kxmall.order.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 评论管理业务对象 kx_store_appraise
 *
 * @author kxmall
 * @date 2023-08-27
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxStoreAppraiseBo extends BaseEntity {

    /**
     * 主键
     */
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
