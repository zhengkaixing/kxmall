package com.kxmall.web.controller.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kxmall.product.domain.KxStoreProductAttrResult;
import com.kxmall.product.domain.bo.KxStoreProductAttrResultBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kxmall.product.domain.vo.KxStoreProductAttrResultVo;
import com.kxmall.product.mapper.KxStoreProductAttrResultMapper;
import com.kxmall.web.controller.product.service.IKxStoreProductAttrResultService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 商品属性详情Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-13
 */
@RequiredArgsConstructor
@Service
public class KxStoreProductAttrResultServiceImpl implements IKxStoreProductAttrResultService {

    private final KxStoreProductAttrResultMapper baseMapper;

    /**
     * 查询商品属性详情
     */
    @Override
    public KxStoreProductAttrResultVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品属性详情列表
     */
    @Override
    public TableDataInfo<KxStoreProductAttrResultVo> queryPageList(KxStoreProductAttrResultBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStoreProductAttrResult> lqw = buildQueryWrapper(bo);
        Page<KxStoreProductAttrResultVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品属性详情列表
     */
    @Override
    public List<KxStoreProductAttrResultVo> queryList(KxStoreProductAttrResultBo bo) {
        LambdaQueryWrapper<KxStoreProductAttrResult> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStoreProductAttrResult> buildQueryWrapper(KxStoreProductAttrResultBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxStoreProductAttrResult> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, KxStoreProductAttrResult::getProductId, bo.getProductId());
        lqw.eq(StringUtils.isNotBlank(bo.getResult()), KxStoreProductAttrResult::getResult, bo.getResult());
        lqw.eq(bo.getChangeTime() != null, KxStoreProductAttrResult::getChangeTime, bo.getChangeTime());
        return lqw;
    }

    /**
     * 新增商品属性详情
     */
    @Override
    public Boolean insertByBo(KxStoreProductAttrResultBo bo) {
        KxStoreProductAttrResult add = BeanUtil.toBean(bo, KxStoreProductAttrResult.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品属性详情
     */
    @Override
    public Boolean updateByBo(KxStoreProductAttrResultBo bo) {
        KxStoreProductAttrResult update = BeanUtil.toBean(bo, KxStoreProductAttrResult.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStoreProductAttrResult entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品属性详情
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
