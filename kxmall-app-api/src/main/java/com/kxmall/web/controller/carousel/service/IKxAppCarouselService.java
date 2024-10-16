package com.kxmall.web.controller.carousel.service;

import com.kxmall.carousel.domain.KxCarousel;

import java.util.List;

/**
 * @author 郅兴开源团队-小黑
 * @version 1.0
 * @date 2023/9/3
 */
public interface IKxAppCarouselService {

    /**
     * 查询所有的广告内容
     *
     * @return
     */
    List<KxCarousel> listAll(Integer adType);

}
