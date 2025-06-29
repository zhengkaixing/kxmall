package com.kxmall.web.controller.recommend.service;

import com.kxmall.carousel.domain.KxCarousel;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.recommend.domain.vo.KxRecommendVo;

import java.util.List;

/**
 * @author 郅兴开源团队-小黑
 * @version 1.0
 * @date 2023/9/3
 */
public interface IKxAppRecommendService {

    TableDataInfo<KxRecommendVo> getRecommendByType(Long storageId, Integer recommendType, Integer pageNo, Integer pageSize);

    /**
     * 获取枚举
     *
     * @return
     */
    List<KxCarousel> getRecommendTypeEnums();

}
