package com.kxmall.web.controller.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kxmall.product.domain.KxStoreProductRule;
import com.kxmall.product.domain.bo.KxStoreProductRuleBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kxmall.product.domain.vo.KxStoreProductRuleVo;
import com.kxmall.product.mapper.KxStoreProductRuleMapper;
import com.kxmall.web.controller.product.service.IKxStoreProductRuleService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 商品规格Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-08
 */
@RequiredArgsConstructor
@Service
public class KxStoreProductRuleServiceImpl implements IKxStoreProductRuleService {

    private final KxStoreProductRuleMapper baseMapper;

    /**
     * 查询商品规格
     */
    @Override
    public KxStoreProductRuleVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品规格列表
     */
    @Override
    public TableDataInfo<KxStoreProductRuleVo> queryPageList(KxStoreProductRuleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStoreProductRule> lqw = buildQueryWrapper(bo);
        Page<KxStoreProductRuleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品规格列表
     */
    @Override
    public List<KxStoreProductRuleVo> queryList(KxStoreProductRuleBo bo) {
        LambdaQueryWrapper<KxStoreProductRule> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStoreProductRule> buildQueryWrapper(KxStoreProductRuleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxStoreProductRule> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getRuleName()), KxStoreProductRule::getRuleName, bo.getRuleName());
        return lqw;
    }

    /**
     * 新增商品规格
     */
    @Override
    public Boolean insertByBo(KxStoreProductRuleBo bo) {
        KxStoreProductRule add = BeanUtil.toBean(bo, KxStoreProductRule.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品规格
     */
    @Override
    public Boolean updateByBo(KxStoreProductRuleBo bo) {
        KxStoreProductRule update = BeanUtil.toBean(bo, KxStoreProductRule.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStoreProductRule entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品规格
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<KxStoreProductRuleVo> queryListAll() {
      return baseMapper.selectVoList(null);
    }
}
