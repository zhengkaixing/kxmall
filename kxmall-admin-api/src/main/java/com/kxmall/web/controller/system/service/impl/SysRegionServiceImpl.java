package com.kxmall.web.controller.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kxmall.system.domain.bo.SysRegionBo;
import com.kxmall.system.domain.vo.SysRegionVo;
import com.kxmall.system.domain.SysRegion;
import com.kxmall.system.mapper.SysRegionMapper;
import com.kxmall.web.controller.system.service.ISysRegionService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 中国地区系统Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-08
 */
@RequiredArgsConstructor
@Service
public class SysRegionServiceImpl implements ISysRegionService {

    private final SysRegionMapper baseMapper;

    /**
     * 查询中国地区系统
     */
    @Override
    public SysRegionVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询中国地区系统列表
     */
    @Override
    public TableDataInfo<SysRegionVo> queryPageList(SysRegionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysRegion> lqw = buildQueryWrapper(bo);
        Page<SysRegionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询中国地区系统列表
     */
    @Override
    public List<SysRegionVo> queryList(SysRegionBo bo) {
        LambdaQueryWrapper<SysRegion> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysRegion> buildQueryWrapper(SysRegionBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysRegion> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getCityId() != null, SysRegion::getCityId, bo.getCityId());
        lqw.eq(bo.getLevel() != null, SysRegion::getLevel, bo.getLevel());
        lqw.eq(bo.getParentId() != null, SysRegion::getParentId, bo.getParentId());
        lqw.eq(StringUtils.isNotBlank(bo.getAreaCode()), SysRegion::getAreaCode, bo.getAreaCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), SysRegion::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getMergerName()), SysRegion::getMergerName, bo.getMergerName());
        lqw.eq(StringUtils.isNotBlank(bo.getLng()), SysRegion::getLng, bo.getLng());
        lqw.eq(StringUtils.isNotBlank(bo.getLat()), SysRegion::getLat, bo.getLat());
        lqw.eq(bo.getIsShow() != null, SysRegion::getIsShow, bo.getIsShow());
        return lqw;
    }

    /**
     * 新增中国地区系统
     */
    @Override
    public Boolean insertByBo(SysRegionBo bo) {
        SysRegion add = BeanUtil.toBean(bo, SysRegion.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改中国地区系统
     */
    @Override
    public Boolean updateByBo(SysRegionBo bo) {
        SysRegion update = BeanUtil.toBean(bo, SysRegion.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysRegion entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除中国地区系统
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
