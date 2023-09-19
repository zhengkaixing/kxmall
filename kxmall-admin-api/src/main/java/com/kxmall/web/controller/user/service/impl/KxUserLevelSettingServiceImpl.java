package com.kxmall.web.controller.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.user.mapper.KxUserLevelSettingMapper;
import com.kxmall.web.controller.user.service.IKxUserLevelSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kxmall.user.domain.bo.KxUserLevelSettingBo;
import com.kxmall.user.domain.vo.KxUserLevelSettingVo;
import com.kxmall.user.domain.KxUserLevelSetting;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 设置用户等级Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-21
 */
@RequiredArgsConstructor
@Service
public class KxUserLevelSettingServiceImpl implements IKxUserLevelSettingService {

    private final KxUserLevelSettingMapper baseMapper;

    /**
     * 查询设置用户等级
     */
    @Override
    public KxUserLevelSettingVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询设置用户等级列表
     */
    @Override
    public TableDataInfo<KxUserLevelSettingVo> queryPageList(KxUserLevelSettingBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxUserLevelSetting> lqw = buildQueryWrapper(bo);
        Page<KxUserLevelSettingVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设置用户等级列表
     */
    @Override
    public List<KxUserLevelSettingVo> queryList(KxUserLevelSettingBo bo) {
        LambdaQueryWrapper<KxUserLevelSetting> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxUserLevelSetting> buildQueryWrapper(KxUserLevelSettingBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxUserLevelSetting> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getMerId() != null, KxUserLevelSetting::getMerId, bo.getMerId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), KxUserLevelSetting::getName, bo.getName());
        lqw.eq(bo.getMoney() != null, KxUserLevelSetting::getMoney, bo.getMoney());
        lqw.eq(bo.getValidDate() != null, KxUserLevelSetting::getValidDate, bo.getValidDate());
        lqw.eq(bo.getIsForever() != null, KxUserLevelSetting::getIsForever, bo.getIsForever());
        lqw.eq(bo.getIsPay() != null, KxUserLevelSetting::getIsPay, bo.getIsPay());
        lqw.eq(bo.getIsShow() != null, KxUserLevelSetting::getIsShow, bo.getIsShow());
        lqw.eq(bo.getGrade() != null, KxUserLevelSetting::getGrade, bo.getGrade());
        lqw.eq(bo.getDiscount() != null, KxUserLevelSetting::getDiscount, bo.getDiscount());
        lqw.eq(CollectionUtils.isNotEmpty(bo.getImage()), KxUserLevelSetting::getImage, bo.getImage());
        lqw.eq(CollectionUtils.isNotEmpty(bo.getIcon()), KxUserLevelSetting::getIcon, bo.getIcon());
        lqw.eq(StringUtils.isNotBlank(bo.getExplain()), KxUserLevelSetting::getExplain, bo.getExplain());
        lqw.eq(bo.getIsDel() != null, KxUserLevelSetting::getIsDel, bo.getIsDel());
        return lqw;
    }

    /**
     * 新增设置用户等级
     */
    @Override
    public Boolean insertByBo(KxUserLevelSettingBo bo) {
        KxUserLevelSetting add = BeanUtil.toBean(bo, KxUserLevelSetting.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设置用户等级
     */
    @Override
    public Boolean updateByBo(KxUserLevelSettingBo bo) {
        KxUserLevelSetting update = BeanUtil.toBean(bo, KxUserLevelSetting.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxUserLevelSetting entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除设置用户等级
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
