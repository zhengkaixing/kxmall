package com.kxmall.web.controller.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kxmall.user.domain.bo.KxUserLevelBo;
import com.kxmall.user.domain.vo.KxUserLevelVo;
import com.kxmall.user.domain.KxUserLevel;
import com.kxmall.user.mapper.KxUserLevelMapper;
import com.kxmall.web.controller.user.service.IKxUserLevelService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户等级Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-14
 */
@RequiredArgsConstructor
@Service
public class KxUserLevelServiceImpl implements IKxUserLevelService {

    private final KxUserLevelMapper baseMapper;

    /**
     * 查询用户等级
     */
    @Override
    public KxUserLevelVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户等级列表
     */
    @Override
    public TableDataInfo<KxUserLevelVo> queryPageList(KxUserLevelBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxUserLevel> lqw = buildQueryWrapper(bo);
        Page<KxUserLevelVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户等级列表
     */
    @Override
    public List<KxUserLevelVo> queryList(KxUserLevelBo bo) {
        LambdaQueryWrapper<KxUserLevel> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxUserLevel> buildQueryWrapper(KxUserLevelBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxUserLevel> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getMerId() != null, KxUserLevel::getMerId, bo.getMerId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), KxUserLevel::getName, bo.getName());
        lqw.eq(bo.getMoney() != null, KxUserLevel::getMoney, bo.getMoney());
        lqw.eq(bo.getValidDate() != null, KxUserLevel::getValidDate, bo.getValidDate());
        lqw.eq(bo.getIsForever() != null, KxUserLevel::getIsForever, bo.getIsForever());
        lqw.eq(bo.getIsPay() != null, KxUserLevel::getIsPay, bo.getIsPay());
        lqw.eq(bo.getIsShow() != null, KxUserLevel::getIsShow, bo.getIsShow());
        lqw.eq(bo.getGrade() != null, KxUserLevel::getGrade, bo.getGrade());
        lqw.eq(bo.getDiscount() != null, KxUserLevel::getDiscount, bo.getDiscount());
        lqw.eq(StringUtils.isNotBlank(bo.getImage()), KxUserLevel::getImage, bo.getImage());
        lqw.eq(StringUtils.isNotBlank(bo.getIcon()), KxUserLevel::getIcon, bo.getIcon());
        lqw.eq(StringUtils.isNotBlank(bo.getExplain()), KxUserLevel::getExplain, bo.getExplain());
        lqw.eq(bo.getIsDel() != null, KxUserLevel::getIsDel, bo.getIsDel());
        return lqw;
    }

    /**
     * 新增用户等级
     */
    @Override
    public Boolean insertByBo(KxUserLevelBo bo) {
        KxUserLevel add = BeanUtil.toBean(bo, KxUserLevel.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户等级
     */
    @Override
    public Boolean updateByBo(KxUserLevelBo bo) {
        KxUserLevel update = BeanUtil.toBean(bo, KxUserLevel.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxUserLevel entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户等级
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
