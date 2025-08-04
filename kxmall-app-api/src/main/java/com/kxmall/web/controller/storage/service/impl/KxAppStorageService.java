package com.kxmall.web.controller.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kxmall.carousel.domain.KxCarousel;
import com.kxmall.carousel.domain.vo.KxCarouselVo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.RecommendType;
import com.kxmall.common.enums.StorageBusinessStatusType;
import com.kxmall.common.enums.StorageStatusType;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.newtimes.domain.KxNewTimes;
import com.kxmall.newtimes.mapper.KxNewTimesMapper;
import com.kxmall.product.domain.vo.KxStoreProductVo;
import com.kxmall.recommend.domain.vo.KxRecommendVo;
import com.kxmall.storage.domain.KxStorage;
import com.kxmall.storage.domain.bo.PointBo;
import com.kxmall.storage.domain.vo.IntegralIndexDataVo;
import com.kxmall.storage.domain.vo.KxStorageVo;
import com.kxmall.storage.domain.vo.RecentlyStorageVo;
import com.kxmall.storage.mapper.KxStorageMapper;
import com.kxmall.web.controller.carousel.service.IKxAppCarouselService;
import com.kxmall.web.controller.product.service.IKxAppProductService;
import com.kxmall.web.controller.recommend.service.IKxAppRecommendService;
import com.kxmall.web.controller.storage.service.IKxAppStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.kxmall.common.constant.ProductCacheConstants.STORAGE_INFO_PREFIX;

/**
 * @author 郅兴开源团队-小黑
 * @version 1.0
 * @date 2023/9/3
 */
@RequiredArgsConstructor
@Service
public class KxAppStorageService implements IKxAppStorageService {


    private final KxStorageMapper storageMapper;
    private final KxNewTimesMapper newTimesMapper;

    private final IKxAppRecommendService recommendService;
    private final IKxAppProductService productService;
    private final IKxAppCarouselService carouselService;

    @Override
    public RecentlyStorageVo getRecentlyStorage(BigDecimal longitude, BigDecimal latitude) {
        RecentlyStorageVo recentlyStorageVo = new RecentlyStorageVo();
        // 获取当前时间
        LocalTime now = LocalTime.now();
        // 获取当前区域范围的仓库
        List<KxStorageVo> storageList = RedisUtils.getCacheList(STORAGE_INFO_PREFIX);
        if (storageList == null || storageList.isEmpty()) {
            LambdaQueryWrapper<KxStorage> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(KxStorage::getState, StorageStatusType.NOMRAL.getCode());
            storageList = storageMapper.selectVoList(wrapper);
            if (storageList != null && !storageList.isEmpty()) {
                for (KxStorageVo kxStorageVo : storageList) {
                    List<List<PointBo>> paths = kxStorageVo.getPaths();
                    if (paths!=null && !paths.isEmpty()) {
                        List<List<PointBo>> polygonList = new LinkedList<>();
                        for (List<PointBo> pathList : paths) {
                            ObjectMapper mapper = new ObjectMapper();
                            List<PointBo> pathListTwo= mapper.convertValue(pathList, new TypeReference<List<PointBo>>() {});
                            polygonList.add(pathListTwo);
                        }
                        kxStorageVo.setPaths(polygonList);
                    }
                }
                RedisUtils.setCacheList(STORAGE_INFO_PREFIX, storageList);
            }
        }
        if (storageList != null && !storageList.isEmpty()) {
            // 获取有效配送范围内的仓库
            double userStorgaeDistance;
            double[] userGps = {latitude.doubleValue(), longitude.doubleValue()};
            Map<Double, KxStorageVo> distanceKxStorageMap = new HashMap<>(storageList.size());
            for (KxStorageVo storageDO : storageList) {

                //判断是否在营业时间范围内
                // 转换营业时间为 LocalTime
                LocalTime businessStart = LocalTime.parse(storageDO.getBusinessStartTime());
                LocalTime businessStop = LocalTime.parse(storageDO.getBusinessStopTime());
                // 判断当前时间是否在营业时间范围内
                if (now.isBefore(businessStart) || now.isAfter(businessStop)) {
                    // 当前时间不在营业时间范围内
                    continue;
                }


                userStorgaeDistance = calculationDistance(new double[]{storageDO.getLatitude().doubleValue(), storageDO.getLongitude().doubleValue()}, userGps);
                // 公里范围校验
                if (userStorgaeDistance <= storageDO.getDeliveryRadius() * 1000) {
                    distanceKxStorageMap.put(userStorgaeDistance, storageDO);
                }
            }
            if (distanceKxStorageMap.size() > 0) {
                KxStorageVo storageDO;
                double distance = 0D;
                KxStorageVo recentlyStorage = null;
                Map.Entry<Double, KxStorageVo> currentEntry;
                distanceKxStorageMap = sortMapByValue(distanceKxStorageMap);
                // 获取有效仓库
                Iterator<Map.Entry<Double, KxStorageVo>> iterator = distanceKxStorageMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    currentEntry = iterator.next();
                    storageDO = currentEntry.getValue();
                    distance = currentEntry.getKey();
                    if (StorageBusinessStatusType.BUSINESS.getCode() == storageDO.getOperatingState()) {
                        recentlyStorage = storageDO;
                        recentlyStorage.setDistance(BigDecimal.valueOf(distance));
                        break;
                    }
                }
                if (recentlyStorage != null) {
                    // 有仓库，营业中
                    recentlyStorageVo.setId(recentlyStorage.getId());
                    recentlyStorageVo.setHaveStorage(true);
                    recentlyStorageVo.setBusinessState(true);
                    recentlyStorageVo.setBusinessStartTime(recentlyStorage.getBusinessStartTime());
                    recentlyStorageVo.setBusinessStopTime(recentlyStorage.getBusinessStopTime());
                    recentlyStorageVo.setDeliveryStartTime(recentlyStorage.getDeliveryStartTime());
                    recentlyStorageVo.setDeliveryStopTime(recentlyStorage.getDeliveryStopTime());
                    recentlyStorageVo.setDistance(recentlyStorage.getDistance());
                } else {
                    // 有仓库，没营业
                    Collection<Double> collection = distanceKxStorageMap.keySet();
                    Double mapKeyMin = Collections.min(collection);
                    recentlyStorage = distanceKxStorageMap.get(mapKeyMin);
                    recentlyStorageVo.setId(recentlyStorage.getId());
                    recentlyStorageVo.setHaveStorage(true);
                    recentlyStorageVo.setBusinessState(false);
                    recentlyStorageVo.setBusinessStartTime(recentlyStorage.getBusinessStartTime());
                    recentlyStorageVo.setBusinessStopTime(recentlyStorage.getBusinessStopTime());
                    recentlyStorageVo.setDeliveryStartTime(recentlyStorage.getDeliveryStartTime());
                    recentlyStorageVo.setDeliveryStopTime(recentlyStorage.getDeliveryStopTime());
                    recentlyStorageVo.setDistance(recentlyStorage.getDistance());
                }
            } else {
                recentlyStorageVo.setHaveStorage(false);
            }
        } else {
            // 没仓库
            recentlyStorageVo.setHaveStorage(false);
        }
        // TODO 测试数据写死仓库
        // ==============================
        KxStorageVo kxStorageVo = storageMapper.selectVoById(11L);
        recentlyStorageVo.setId(11L);
        recentlyStorageVo.setBusinessStartTime(kxStorageVo.getBusinessStartTime());
        recentlyStorageVo.setBusinessStopTime(kxStorageVo.getBusinessStopTime());
        recentlyStorageVo.setDeliveryStartTime(kxStorageVo.getDeliveryStartTime());
        recentlyStorageVo.setDeliveryStopTime(kxStorageVo.getDeliveryStopTime());
        double[] userGps = {latitude.doubleValue(), longitude.doubleValue()};
        recentlyStorageVo.setDistance(BigDecimal.valueOf(calculationDistance(new double[]{kxStorageVo.getLatitude().doubleValue(), kxStorageVo.getLongitude().doubleValue()}, userGps)));
        recentlyStorageVo.setHaveStorage(true);
        recentlyStorageVo.setBusinessState(true);
        //===============================
        return recentlyStorageVo;
    }

    @Override
    public IntegralIndexDataVo getIndexDataByStorage(Long storageId) {
        //分类
        List<KxCarousel> carouselList = carouselService.listAll(99);
        Map<String, List<KxCarouselVo>> listMap = carouselList.stream().map(item -> {
            KxCarouselVo kxCarouselVo = new KxCarouselVo();
            BeanUtils.copyProperties(item, kxCarouselVo);
            return kxCarouselVo;
        }).collect(Collectors.groupingBy(item -> "t" + item.getAdType()));

//        List<KxCarouselVo> categoryPickAd = listMap.get("t" + AdvertisementType.CATEGORY_PICK.getCode());
//        //封装 分类精选 商品
//        if (!CollectionUtils.isEmpty(categoryPickAd)) {
//            Page<KxStoreProductVo> pickPage;
//            for (KxCarouselVo item : categoryPickAd) {
//                pickPage = productService.getGoodsPage(1, 10, new Long(item.getUrl().substring(item.getUrl().lastIndexOf("=") + 1)), "sales", false, null);
//                item.setData(pickPage.getRecords());
//            }
//        }

        IntegralIndexDataVo integralIndexDataVo = new IntegralIndexDataVo();
        integralIndexDataVo.setCarouseList(listMap);

        // 销量冠军
        List<KxRecommendVo> salesRecommend = recommendService.getRecommendByType(storageId, RecommendType.HOT.getCode(), 1, 10).getRows();
        integralIndexDataVo.setSalesTop(salesRecommend);

        // 特价推荐
        List<KxRecommendVo> cheapRecommend = recommendService.getRecommendByType(storageId, RecommendType.CHEAP.getCode(), 1, 10).getRows();
        integralIndexDataVo.setCheapRecommend(cheapRecommend);

        // 最近上新
        List<KxStoreProductVo> newTop = productService.getGoodsPageByStorage(storageId, 1, 10, null, "id", false, null,0).getRows();
        integralIndexDataVo.setNewTop(newTop);

        // 新鲜时报
        integralIndexDataVo.setNewTimesContent(getTimes(storageId));
        return integralIndexDataVo;
    }


    /**
     * 获取新鲜时报
     * @param storageId
     * @return
     */
    private String getTimes(Long storageId) {
        KxNewTimes newTimes = newTimesMapper.selectOne(new LambdaQueryWrapper<KxNewTimes>()
                .eq(KxNewTimes::getStorageId, storageId)
                .eq(KxNewTimes::getIsStop, 1));
        if (ObjectUtils.isEmpty(newTimes)) {
            return "您想不到的新鲜时蔬马上就来.";
        }
        return newTimes.getContent();
    }

    @Override
    public KxStorageVo getStorage(Long storageId) {
        return storageMapper.selectVoById(storageId);
    }


    @Override
    public TableDataInfo<KxRecommendVo> getRecommendByStorage(Long storageId, Integer recommendType, Integer pageNo, Integer pageSize) {
        return recommendService.getRecommendByType(storageId, recommendType, pageNo, pageSize);
    }


    /**
     * 计算两点距离
     *
     * @param point1 点1
     * @param point2 点2
     * @return 两点之间的距离(米)
     */
    public static double calculationDistance(double[] point1, double[] point2) {
        double lat1 = point1[0];
        double lat2 = point2[0];
        double lng1 = point1[1];
        double lng2 = point2[1];
        double radLat1 = lat1 * Math.PI / 180.0;
        double radLat2 = lat2 * Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = (lng1 * Math.PI / 180.0) - (lng2 * Math.PI / 180.0);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        return s * 6370996.81;
    }

    /**
     * 根据Key-Map排序
     *
     * @param oriMap 需要排序的Map
     * @return 排序后的Map
     */
    public static Map<Double, KxStorageVo> sortMapByValue(Map<Double, KxStorageVo> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<Double, KxStorageVo> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<Double, KxStorageVo>> entryList = new ArrayList<>(oriMap.entrySet());
        entryList.sort((me1, me2) -> me2.getKey().compareTo(me1.getKey()));
        Iterator<Map.Entry<Double, KxStorageVo>> iter = entryList.iterator();
        Map.Entry<Double, KxStorageVo> tmpEntry;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }
}
