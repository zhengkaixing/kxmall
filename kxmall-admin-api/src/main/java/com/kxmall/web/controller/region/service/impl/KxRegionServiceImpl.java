package com.kxmall.web.controller.region.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.region.domain.KxRegion;
import com.kxmall.region.domain.bo.KxRegionBo;
import com.kxmall.region.domain.vo.KxRegionVo;
import com.kxmall.region.mapper.KxRegionMapper;
import com.kxmall.web.controller.region.service.IKxRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 中国地区信息Service业务层处理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@RequiredArgsConstructor
@Service
public class KxRegionServiceImpl implements IKxRegionService {

    private final KxRegionMapper baseMapper;


    private static final Integer PROVINCE_LEVEL = 1;
    private static final Integer CITY_LEVEL = 2;
    private static final Integer COUNTY_LEVEL = 3;
    private static final String PROVINCE_INFO_DATA = "PROVINCE_INFO_DATA";
    private static final String CITY_INFO_DATA = "CITY_INFO_DATA";
    private static final String COUNTY_INFO_DATA = "COUNTY_INFO_DATA";
    private static final String CITY_INFO_DATA_PREFIX = "CITY_INFO_DATA_PREFIX_";
    private static final String COUNTY_INFO_DATA_PREFIX = "COUNTY_INFO_DATA_PREFIX_";

    /**
     * 查询中国地区信息
     */
    @Override
    public KxRegionVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询中国地区信息列表
     */
    @Override
    public TableDataInfo<KxRegionVo> queryPageList(KxRegionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxRegion> lqw = buildQueryWrapper(bo);
        Page<KxRegionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询中国地区信息列表
     */
    @Override
    public List<KxRegionVo> queryList(KxRegionBo bo) {
        LambdaQueryWrapper<KxRegion> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxRegion> buildQueryWrapper(KxRegionBo bo) {
        LambdaQueryWrapper<KxRegion> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), KxRegion::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), KxRegion::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getShortName()), KxRegion::getShortName, bo.getShortName());
        lqw.eq(StringUtils.isNotBlank(bo.getSuperiorCode()), KxRegion::getSuperiorCode, bo.getSuperiorCode());
        lqw.eq(StringUtils.isNotBlank(bo.getLng()), KxRegion::getLng, bo.getLng());
        lqw.eq(StringUtils.isNotBlank(bo.getLat()), KxRegion::getLat, bo.getLat());
        lqw.eq(bo.getSort() != null, KxRegion::getSort, bo.getSort());
        lqw.eq(StringUtils.isNotBlank(bo.getRamark()), KxRegion::getRamark, bo.getRamark());
        lqw.eq(bo.getState() != null, KxRegion::getState, bo.getState());
        lqw.eq(StringUtils.isNotBlank(bo.getTenantCode()), KxRegion::getTenantCode, bo.getTenantCode());
        lqw.eq(bo.getLevel() != null, KxRegion::getLevel, bo.getLevel());
        return lqw;
    }

    /**
     * 新增中国地区信息
     */
    @Override
    public Boolean insertByBo(KxRegionBo bo) {
        KxRegion add = BeanUtil.toBean(bo, KxRegion.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改中国地区信息
     */
    @Override
    public Boolean updateByBo(KxRegionBo bo) {
        KxRegion update = BeanUtil.toBean(bo, KxRegion.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxRegion entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除中国地区信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<Map<String, Object>> getProvinceAll() {
        List<KxRegion> regionDOList = RedisUtils.getCacheList(PROVINCE_INFO_DATA);
        if (regionDOList == null || regionDOList.size() == 0) {
            LambdaQueryWrapper<KxRegion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(KxRegion::getLevel, PROVINCE_LEVEL);
            lambdaQueryWrapper.orderByAsc(KxRegion::getSort);
            regionDOList = baseMapper.selectList(lambdaQueryWrapper);
            if (regionDOList != null && regionDOList.size() > 0) {
                RedisUtils.setCacheList(PROVINCE_INFO_DATA, regionDOList);
            }
        }
        List<Map<String, Object>> list = buildRegionListMapInfo(regionDOList);
        if (list != null) {
            return list;
        }
        throw new ServiceException("行政省为空!");
    }

    @Override
    public List<Map<String, Object>> getCityAll() {
        List<KxRegion> regionDOList = RedisUtils.getCacheList(CITY_INFO_DATA);
        if (regionDOList == null || regionDOList.size() == 0) {
            LambdaQueryWrapper<KxRegion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(KxRegion::getLevel, CITY_LEVEL);
            lambdaQueryWrapper.orderByAsc(KxRegion::getSort);
            regionDOList = baseMapper.selectList(lambdaQueryWrapper);
            if (regionDOList != null && regionDOList.size() > 0) {
                RedisUtils.setCacheList(CITY_INFO_DATA, regionDOList);
            }
        }
        List<Map<String, Object>> list = buildRegionListMapInfo(regionDOList);
        if (list != null) {
            return list;
        }
        throw new ServiceException("行政市为空！");
    }

    @Override
    public List<Map<String, Object>> getCountyAll() {
        List<KxRegion> regionDOList = RedisUtils.getCacheList(COUNTY_INFO_DATA);
        if (regionDOList == null || regionDOList.size() == 0) {
            LambdaQueryWrapper<KxRegion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(KxRegion::getLevel, COUNTY_LEVEL);
            lambdaQueryWrapper.orderByAsc(KxRegion::getSort);
            regionDOList = baseMapper.selectList(lambdaQueryWrapper);
            if (regionDOList != null && regionDOList.size() > 0) {
                RedisUtils.setCacheList(COUNTY_INFO_DATA, regionDOList);
            }
        }
        List<Map<String, Object>> list = buildRegionListMapInfo(regionDOList);
        if (list != null) {
            return list;
        }
        throw new ServiceException("行政区（县）为空！");
    }

    @Override
    public List<Map<String, Object>> getCity(Long provinceId) {
        List<KxRegion> regionDOList = RedisUtils.getCacheList(CITY_INFO_DATA_PREFIX + provinceId);
        if (regionDOList == null || regionDOList.size() == 0) {
            KxRegion provinceRegionDO = baseMapper.selectById(provinceId);
            if (provinceRegionDO != null) {
                LambdaQueryWrapper<KxRegion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(KxRegion::getLevel, CITY_LEVEL);
                lambdaQueryWrapper.eq(KxRegion::getSuperiorCode, provinceRegionDO.getCode());
                lambdaQueryWrapper.orderByAsc(KxRegion::getSort);
                regionDOList = baseMapper.selectList(lambdaQueryWrapper);
                if (regionDOList != null && regionDOList.size() > 0) {
                    RedisUtils.setCacheList(CITY_INFO_DATA_PREFIX + provinceId, regionDOList);
                }
            }
        }
        List<Map<String, Object>> list = buildRegionListMapInfo(regionDOList);
        if (list != null) {
            return list;
        }
        throw new ServiceException("行政市为空！");
    }

    @Override
    public List<Map<String, Object>> getCounty(Long cityId) {
        List<KxRegion> regionDOList = RedisUtils.getCacheList(COUNTY_INFO_DATA_PREFIX + cityId);
        if (regionDOList == null || regionDOList.size() == 0) {
            KxRegion cityRegionDO = baseMapper.selectById(cityId);
            if (cityRegionDO != null) {
                LambdaQueryWrapper<KxRegion> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(KxRegion::getLevel, COUNTY_LEVEL);
                lambdaQueryWrapper.eq(KxRegion::getSuperiorCode, cityRegionDO.getCode());
                lambdaQueryWrapper.orderByAsc(KxRegion::getSort);
                regionDOList = baseMapper.selectList(lambdaQueryWrapper);
                if (regionDOList != null && regionDOList.size() > 0) {
                    RedisUtils.setCacheList(COUNTY_INFO_DATA_PREFIX + cityId, regionDOList);
                }
            }
        }
        List<Map<String, Object>> list = buildRegionListMapInfo(regionDOList);
        if (list != null) {
            return list;
        }
        throw new ServiceException("行政区（县）为空！");
    }

    private List<Map<String, Object>> buildRegionListMapInfo(List<KxRegion> regionDOList) {
        if (regionDOList != null && regionDOList.size() > 0) {
            List<Map<String, Object>> list = new LinkedList<>();
            regionDOList.forEach(item -> {
                Map<String, Object> map = new HashMap<>();
                map.put("value", item.getId());
                map.put("label", item.getName());
                list.add(map);
            });
            return list;
        }
        return null;
    }
}
