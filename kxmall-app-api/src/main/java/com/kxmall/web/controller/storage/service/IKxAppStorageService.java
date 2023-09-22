package com.kxmall.web.controller.storage.service;

import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.recommend.domain.vo.KxRecommendVo;
import com.kxmall.storage.domain.vo.IntegralIndexDataVo;
import com.kxmall.storage.domain.vo.KxStorageVo;
import com.kxmall.storage.domain.vo.RecentlyStorageVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/3
 */
public interface IKxAppStorageService {

    /**
     * 获取最近的仓库的数据
     * @param longitude
     * @param latitude
     * @return
     */
    RecentlyStorageVo getRecentlyStorage(BigDecimal longitude, BigDecimal latitude);


    /**
     * 获取指定仓库数据内容
     */
    IntegralIndexDataVo getIndexDataByStorage(Long storageId);

    /**
     * 获取仓库信息
     * @param storageId
     * @return
     */
    KxStorageVo getStorage(Long storageId);

    /**
     * 获取最近的仓库推荐内容
     * @param storageId
     * @param recommedType
     * @param pageNo
     * @param pageSize
     * @return
     */
    TableDataInfo<KxRecommendVo> getRecommendByStorage(Long storageId, Integer recommedType, Integer pageNo, Integer pageSize);
}
