package com.kxmall.carousel.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商铺广告对象 kx_carousel
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_carousel")
public class KxCarousel extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
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
