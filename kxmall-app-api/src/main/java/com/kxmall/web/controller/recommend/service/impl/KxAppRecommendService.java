package com.kxmall.web.controller.recommend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.kxmall.common.core.domain.entity.SysDictData;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.recommend.domain.vo.KxRecommendVo;
import com.kxmall.recommend.mapper.KxRecommendMapper;
import com.kxmall.system.mapper.SysDictDataMapper;
import com.kxmall.web.controller.recommend.service.IKxAppRecommendService;
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
public class KxAppRecommendService implements IKxAppRecommendService {

    private final KxRecommendMapper baseMapper;

    private final SysDictDataMapper dictDataMapper;

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
            RedisUtils.setCacheObject(keyCache, tableDataInfo);
        }
        return tableDataInfo;
    }

    @Override
    public List<SysDictData> getRecommendTypeEnums() {
        List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType("recommend_type");
        dictDatas.sort((me1, me2) -> me2.getDictValue().compareTo(me1.getDictValue()));
        if (CollUtil.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        return null;
    }
}
