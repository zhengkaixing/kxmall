package com.kxmall.recommend.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import java.util.List;


/**
 * 推荐管理业务对象 kx_recommend
 *
 * @author kxmall
 * @date 2023-08-27
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxRecommendBo extends BaseEntity {

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
     * 商品id数组
     */
    private List<Long> productIds;

    /**
     * 推荐类型1特价推荐
     */
    private Long recommendType;


}
