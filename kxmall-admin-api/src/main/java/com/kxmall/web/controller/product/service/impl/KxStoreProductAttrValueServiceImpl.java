package com.kxmall.web.controller.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kxmall.product.domain.KxStoreProductAttrValue;
import com.kxmall.product.domain.bo.KxStoreProductAttrValueBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kxmall.product.domain.vo.KxStoreProductAttrValueVo;
import com.kxmall.product.mapper.KxStoreProductAttrValueMapper;
import com.kxmall.web.controller.product.service.IKxStoreProductAttrValueService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 商品属性值Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-13
 */
@RequiredArgsConstructor
@Service
public class KxStoreProductAttrValueServiceImpl implements IKxStoreProductAttrValueService {

    private final KxStoreProductAttrValueMapper baseMapper;

    /**
     * 查询商品属性值
     */
    @Override
    public KxStoreProductAttrValueVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品属性值列表
     */
    @Override
    public TableDataInfo<KxStoreProductAttrValueVo> queryPageList(KxStoreProductAttrValueBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStoreProductAttrValue> lqw = buildQueryWrapper(bo);
        Page<KxStoreProductAttrValueVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品属性值列表
     */
    @Override
    public List<KxStoreProductAttrValueVo> queryList(KxStoreProductAttrValueBo bo) {
        LambdaQueryWrapper<KxStoreProductAttrValue> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStoreProductAttrValue> buildQueryWrapper(KxStoreProductAttrValueBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxStoreProductAttrValue> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, KxStoreProductAttrValue::getProductId, bo.getProductId());
        lqw.eq(StringUtils.isNotBlank(bo.getSku()), KxStoreProductAttrValue::getSku, bo.getSku());
        lqw.eq(bo.getStock() != null, KxStoreProductAttrValue::getStock, bo.getStock());
        lqw.eq(bo.getSales() != null, KxStoreProductAttrValue::getSales, bo.getSales());
        lqw.eq(bo.getPrice() != null, KxStoreProductAttrValue::getPrice, bo.getPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getImage()), KxStoreProductAttrValue::getImage, bo.getImage());
        lqw.eq(StringUtils.isNotBlank(bo.getUnique()), KxStoreProductAttrValue::getUnique, bo.getUnique());
        lqw.eq(bo.getCost() != null, KxStoreProductAttrValue::getCost, bo.getCost());
        lqw.eq(StringUtils.isNotBlank(bo.getBarCode()), KxStoreProductAttrValue::getBarCode, bo.getBarCode());
        lqw.eq(bo.getOtPrice() != null, KxStoreProductAttrValue::getOtPrice, bo.getOtPrice());
        lqw.eq(bo.getWeight() != null, KxStoreProductAttrValue::getWeight, bo.getWeight());
        lqw.eq(bo.getVolume() != null, KxStoreProductAttrValue::getVolume, bo.getVolume());
        lqw.eq(bo.getBrokerage() != null, KxStoreProductAttrValue::getBrokerage, bo.getBrokerage());
        lqw.eq(bo.getBrokerageTwo() != null, KxStoreProductAttrValue::getBrokerageTwo, bo.getBrokerageTwo());
        lqw.eq(bo.getPinkPrice() != null, KxStoreProductAttrValue::getPinkPrice, bo.getPinkPrice());
        lqw.eq(bo.getPinkStock() != null, KxStoreProductAttrValue::getPinkStock, bo.getPinkStock());
        lqw.eq(bo.getSeckillPrice() != null, KxStoreProductAttrValue::getSeckillPrice, bo.getSeckillPrice());
        lqw.eq(bo.getSeckillStock() != null, KxStoreProductAttrValue::getSeckillStock, bo.getSeckillStock());
        lqw.eq(bo.getIntegral() != null, KxStoreProductAttrValue::getIntegral, bo.getIntegral());
        return lqw;
    }

    /**
     * 新增商品属性值
     */
    @Override
    public Boolean insertByBo(KxStoreProductAttrValueBo bo) {
        KxStoreProductAttrValue add = BeanUtil.toBean(bo, KxStoreProductAttrValue.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品属性值
     */
    @Override
    public Boolean updateByBo(KxStoreProductAttrValueBo bo) {
        KxStoreProductAttrValue update = BeanUtil.toBean(bo, KxStoreProductAttrValue.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStoreProductAttrValue entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品属性值
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
