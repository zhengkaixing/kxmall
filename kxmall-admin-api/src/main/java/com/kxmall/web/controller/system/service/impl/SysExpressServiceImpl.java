package com.kxmall.web.controller.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.system.domain.SysExpress;
import com.kxmall.system.domain.bo.SysExpressBo;
import com.kxmall.system.domain.vo.SysExpressVo;
import com.kxmall.system.mapper.SysExpressMapper;
import com.kxmall.web.controller.system.service.ISysExpressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 快递公司Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-17
 */
@RequiredArgsConstructor
@Service
public class SysExpressServiceImpl implements ISysExpressService {

    private final SysExpressMapper baseMapper;

    /**
     * 查询快递公司
     */
    @Override
    public SysExpressVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询快递公司列表
     */
    @Override
    public TableDataInfo<SysExpressVo> queryPageList(SysExpressBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysExpress> lqw = buildQueryWrapper(bo);
        Page<SysExpressVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询快递公司列表
     */
    @Override
    public List<SysExpressVo> queryList(SysExpressBo bo) {
        LambdaQueryWrapper<SysExpress> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysExpress> buildQueryWrapper(SysExpressBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysExpress> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), SysExpress::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), SysExpress::getName, bo.getName());
        lqw.eq(bo.getSort() != null, SysExpress::getSort, bo.getSort());
        lqw.eq(bo.getIsShow() != null, SysExpress::getIsShow, bo.getIsShow());
        lqw.eq(bo.getIsDel() != null, SysExpress::getIsDel, bo.getIsDel());
        return lqw;
    }

    /**
     * 新增快递公司
     */
    @Override
    public Boolean insertByBo(SysExpressBo bo) {
        SysExpress add = BeanUtil.toBean(bo, SysExpress.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改快递公司
     */
    @Override
    public Boolean updateByBo(SysExpressBo bo) {
        SysExpress update = BeanUtil.toBean(bo, SysExpress.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysExpress entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除快递公司
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
