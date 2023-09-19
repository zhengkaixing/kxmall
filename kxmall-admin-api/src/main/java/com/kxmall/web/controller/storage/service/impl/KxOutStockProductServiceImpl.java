package com.kxmall.web.controller.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kxmall.storage.domain.bo.KxOutStockProductBo;
import com.kxmall.storage.domain.vo.KxOutStockProductVo;
import com.kxmall.storage.domain.KxOutStockProduct;
import com.kxmall.storage.mapper.KxOutStockProductMapper;
import com.kxmall.web.controller.storage.service.IKxOutStockProductService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 出库商品Service业务层处理
 *
 * @author kxmall
 * @date 2023-08-29
 */
@RequiredArgsConstructor
@Service
public class KxOutStockProductServiceImpl implements IKxOutStockProductService {

    private final KxOutStockProductMapper baseMapper;

    /**
     * 查询出库商品
     */
    @Override
    public KxOutStockProductVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询出库商品列表
     */
    @Override
    public TableDataInfo<KxOutStockProductVo> queryPageList(KxOutStockProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxOutStockProduct> lqw = buildQueryWrapper(bo);
        Page<KxOutStockProductVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询出库商品列表
     */
    @Override
    public List<KxOutStockProductVo> queryList(KxOutStockProductBo bo) {
        LambdaQueryWrapper<KxOutStockProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxOutStockProduct> buildQueryWrapper(KxOutStockProductBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxOutStockProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCategoryName()), KxOutStockProduct::getCategoryName, bo.getCategoryName());
        lqw.eq(StringUtils.isNotBlank(bo.getBarCode()), KxOutStockProduct::getBarCode, bo.getBarCode());
        lqw.eq(StringUtils.isNotBlank(bo.getOutStockNumbers()), KxOutStockProduct::getOutStockNumbers, bo.getOutStockNumbers());
        lqw.like(StringUtils.isNotBlank(bo.getProductName()), KxOutStockProduct::getProductName, bo.getProductName());
        lqw.like(StringUtils.isNotBlank(bo.getProductAttrName()), KxOutStockProduct::getProductAttrName, bo.getProductAttrName());
        lqw.eq(bo.getStock() != null, KxOutStockProduct::getStock, bo.getStock());
        lqw.eq(bo.getOutStockNum() != null, KxOutStockProduct::getOutStockNum, bo.getOutStockNum());
        lqw.eq(bo.getProductAttrId() != null, KxOutStockProduct::getProductAttrId, bo.getProductAttrId());
        return lqw;
    }

    /**
     * 新增出库商品
     */
    @Override
    public Boolean insertByBo(KxOutStockProductBo bo) {
        KxOutStockProduct add = BeanUtil.toBean(bo, KxOutStockProduct.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改出库商品
     */
    @Override
    public Boolean updateByBo(KxOutStockProductBo bo) {
        KxOutStockProduct update = BeanUtil.toBean(bo, KxOutStockProduct.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxOutStockProduct entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除出库商品
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
