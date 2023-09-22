package com.kxmall.web.controller.recommend.service;

import com.kxmall.common.core.domain.entity.SysDictData;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.recommend.domain.vo.KxRecommendVo;

import java.util.List;

/**
 * @author kaixin
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
    List<SysDictData> getRecommendTypeEnums();

}
