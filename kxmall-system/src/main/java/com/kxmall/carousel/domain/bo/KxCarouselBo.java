package com.kxmall.carousel.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 商铺广告业务对象 kx_carousel
 *
 * @author kxmall
 * @date 2023-08-27
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxCarouselBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 广告类型
     */
    private String adType;

    /**
     * 广告标题
     */
    private String title;

    /**
     * 广告请求路径
     */
    private String url;

    /**
     * 图片路径
     */
    private String imgUrl;

    /**
     * 状态
     */
    private String status;

    /**
     * 站外链接
     */
    private String outUrl;


}
