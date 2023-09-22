package com.kxmall.web.controller.carousel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kxmall.carousel.domain.KxCarousel;
import com.kxmall.carousel.mapper.KxCarouselMapper;
import com.kxmall.common.enums.StatusType;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.web.controller.carousel.service.IKxAppCarouselService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/3
 */
@RequiredArgsConstructor
@Service
public class KxAppCarouselService implements IKxAppCarouselService {

    private final KxCarouselMapper baseMapper;

    private final static String ADVERTISEMENT_NAME = "ADVERTISEMENT_TYPE_";

    @Override
    public List<KxCarousel> listAll(Integer adType) {
        List<KxCarousel> cacheList
            = RedisUtils.getCacheList(ADVERTISEMENT_NAME + adType);
        if (CollectionUtils.isEmpty(cacheList)) {
            LambdaQueryWrapper<KxCarousel> wrapper = new LambdaQueryWrapper<KxCarousel>()
                .eq(KxCarousel::getStatus, StatusType.ACTIVE.getCode());
            if (adType != 99) {
                wrapper.eq(KxCarousel::getAdType, adType);
            }
            cacheList = baseMapper.selectList(wrapper);
            RedisUtils.setCacheList(ADVERTISEMENT_NAME + adType, cacheList);
        }
        return cacheList;
    }
}
