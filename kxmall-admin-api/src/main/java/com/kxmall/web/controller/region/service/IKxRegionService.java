package com.kxmall.web.controller.region.service;

import com.kxmall.region.domain.vo.KxRegionVo;
import com.kxmall.region.domain.bo.KxRegionBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 中国地区信息Service接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface IKxRegionService {

    /**
     * 查询中国地区信息
     */
    KxRegionVo queryById(Long id);

    /**
     * 查询中国地区信息列表
     */
    TableDataInfo<KxRegionVo> queryPageList(KxRegionBo bo, PageQuery pageQuery);

    /**
     * 查询中国地区信息列表
     */
    List<KxRegionVo> queryList(KxRegionBo bo);

    /**
     * 新增中国地区信息
     */
    Boolean insertByBo(KxRegionBo bo);

    /**
     * 修改中国地区信息
     */
    Boolean updateByBo(KxRegionBo bo);

    /**
     * 校验并批量删除中国地区信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取所有省份
     * @return
     */
    List<Map<String, Object>> getProvinceAll();

    List<Map<String, Object>> getCityAll();

    List<Map<String, Object>> getCountyAll();

    List<Map<String, Object>> getCity(Long provinceId);

    List<Map<String, Object>> getCounty(Long cityId);
}
