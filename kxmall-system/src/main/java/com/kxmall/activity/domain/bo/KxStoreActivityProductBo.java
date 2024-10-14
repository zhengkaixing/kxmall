package com.kxmall.activity.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import com.kxmall.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


/**
 * 活动商品业务对象 kx_store_activity_product
 *
 * @author kxmall
 * @date 2024-08-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxStoreActivityProductBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 活动id
     */
    private Long activityId;


    /**
     * 商品id数组
     */
    private List<Long> productIds;


    /**
     * 活动价
     */
    private BigDecimal activityPrice;
}
