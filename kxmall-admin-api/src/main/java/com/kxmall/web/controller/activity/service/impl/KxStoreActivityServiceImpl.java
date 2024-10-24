package com.kxmall.web.controller.activity.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.activity.domain.KxStoreActivity;
import com.kxmall.activity.domain.bo.KxStoreActivityBo;
import com.kxmall.activity.domain.vo.KxStoreActivityVo;
import com.kxmall.activity.mapper.KxStoreActivityMapper;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.web.controller.activity.service.IKxStoreActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 活动商品Service业务层处理
 *
 * @author kxmall
 * @date 2024-08-07
 */
@RequiredArgsConstructor
@Service
public class KxStoreActivityServiceImpl implements IKxStoreActivityService {

    private final KxStoreActivityMapper baseMapper;

    /**
     * 查询活动商品
     */
    @Override
    public KxStoreActivityVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询活动商品列表
     */
    @Override
    public TableDataInfo<KxStoreActivityVo> queryPageList(KxStoreActivityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStoreActivity> lqw = buildQueryWrapper(bo);
        Page<KxStoreActivityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询活动商品列表
     */
    @Override
    public List<KxStoreActivityVo> queryList(KxStoreActivityBo bo) {
        LambdaQueryWrapper<KxStoreActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStoreActivity> buildQueryWrapper(KxStoreActivityBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxStoreActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), KxStoreActivity::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getImgUrl()), KxStoreActivity::getImgUrl, bo.getImgUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getOutUrl()), KxStoreActivity::getOutUrl, bo.getOutUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), KxStoreActivity::getDescription, bo.getDescription());
        lqw.eq(bo.getStatus() != null, KxStoreActivity::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增活动商品
     */
    @Override
    public Boolean insertByBo(KxStoreActivityBo bo) {
        KxStoreActivity add = BeanUtil.toBean(bo, KxStoreActivity.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改活动商品
     */
    @Override
    public Boolean updateByBo(KxStoreActivityBo bo) {
        KxStoreActivity update = BeanUtil.toBean(bo, KxStoreActivity.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStoreActivity entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除活动商品
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
