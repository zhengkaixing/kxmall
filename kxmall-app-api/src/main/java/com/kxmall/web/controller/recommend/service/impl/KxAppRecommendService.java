package com.kxmall.web.controller.recommend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kxmall.carousel.domain.KxCarousel;
import com.kxmall.carousel.mapper.KxCarouselMapper;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.StatusType;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.recommend.domain.vo.KxRecommendVo;
import com.kxmall.recommend.mapper.KxRecommendMapper;
import com.kxmall.system.mapper.SysDictDataMapper;
import com.kxmall.web.controller.recommend.service.IKxAppRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;

/**
 * @author 郅兴开源团队-小黑
 * @version 1.0
 * @date 2023/9/3
 */
@RequiredArgsConstructor
@Service
public class KxAppRecommendService implements IKxAppRecommendService {

    private final KxRecommendMapper baseMapper;

    private final SysDictDataMapper dictDataMapper;

    private final KxCarouselMapper carouselMapper;

    private static final String RECOMMEND_NAME = "RECOMMEND_TYPE_";

    @Override
    public TableDataInfo<KxRecommendVo> getRecommendByType(Long storageId, Integer recommendType, Integer pageNo, Integer pageSize) {
        //缓存key
        String keyCache = RECOMMEND_NAME + recommendType + "_" + storageId + "_" + pageNo + "_" + pageSize;
        //若关键字为空，尝试从缓存取列表
        TableDataInfo<KxRecommendVo> objFromCache = RedisUtils.getCacheObject(keyCache);
        if (objFromCache != null) {
            return objFromCache;
        }
        Integer offset = (pageNo - 1) * pageSize;
        Integer size = pageSize;
        List<KxRecommendVo> recommendDTOList = baseMapper.getRecommendByStorage(storageId, recommendType, offset, size);
        Long count = baseMapper.getRecommendByStorageCount(storageId, recommendType);
        TableDataInfo<KxRecommendVo> tableDataInfo = new TableDataInfo<>(recommendDTOList, count);
        if (!CollectionUtils.isEmpty(recommendDTOList)) {
            RedisUtils.setCacheObject(keyCache, tableDataInfo, Duration.ofDays(1));
        }
        return tableDataInfo;
    }

    @Override
    public List<KxCarousel> getRecommendTypeEnums() {
        LambdaQueryWrapper<KxCarousel> wrapper = new LambdaQueryWrapper<KxCarousel>()
                .eq(KxCarousel::getStatus, StatusType.ACTIVE.getCode());
        wrapper.eq(KxCarousel::getAdType, 4);
        wrapper.orderByDesc(KxCarousel::getSort);
        return carouselMapper.selectList(wrapper);
    }
}
