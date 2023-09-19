package com.kxmall.storage.domain.vo;

import com.kxmall.carousel.domain.vo.KxCarouselVo;
import com.kxmall.product.domain.vo.KxStoreProductVo;
import com.kxmall.recommend.domain.vo.KxRecommendVo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 首页聚合接口DTO
 *
 * @author kaixin
 * @date 2022/7/14
 */
@Data
public class IntegralIndexDataVo {

    private Map<String, List<KxCarouselVo>> carouseList;

    private List<KxRecommendVo> cheapRecommend;

    private List<KxRecommendVo> salesTop;

    private List<KxStoreProductVo> newTop;

    /**
     * 新鲜时报
     */
    private String newTimesContent;

}
