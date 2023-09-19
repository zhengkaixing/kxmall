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
import com.kxmall.storage.domain.bo.KxInStockProductBo;
import com.kxmall.storage.domain.vo.KxInStockProductVo;
import com.kxmall.storage.domain.KxInStockProduct;
import com.kxmall.storage.mapper.KxInStockProductMapper;
import com.kxmall.web.controller.storage.service.IKxInStockProductService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 入库商品Service业务层处理
 *
 * @author kxmall
 * @date 2023-08-29
 */
@RequiredArgsConstructor
@Service
public class KxInStockProductServiceImpl implements IKxInStockProductService {

    private final KxInStockProductMapper baseMapper;

    /**
     * 查询入库商品
     */
    @Override
    public KxInStockProductVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询入库商品列表
     */
    @Override
    public TableDataInfo<KxInStockProductVo> queryPageList(KxInStockProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxInStockProduct> lqw = buildQueryWrapper(bo);
        Page<KxInStockProductVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询入库商品列表
     */
    @Override
    public List<KxInStockProductVo> queryList(KxInStockProductBo bo) {
        LambdaQueryWrapper<KxInStockProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxInStockProduct> buildQueryWrapper(KxInStockProductBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxInStockProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCategoryName()), KxInStockProduct::getCategoryName, bo.getCategoryName());
        lqw.eq(StringUtils.isNotBlank(bo.getBarCode()), KxInStockProduct::getBarCode, bo.getBarCode());
        lqw.eq(StringUtils.isNotBlank(bo.getInStockNumbers()), KxInStockProduct::getInStockNumbers, bo.getInStockNumbers());
        lqw.like(StringUtils.isNotBlank(bo.getProductName()), KxInStockProduct::getProductName, bo.getProductName());
        lqw.like(StringUtils.isNotBlank(bo.getProductAttrName()), KxInStockProduct::getProductAttrName, bo.getProductAttrName());
        lqw.eq(bo.getStock() != null, KxInStockProduct::getStock, bo.getStock());
        lqw.eq(bo.getInStockNum() != null, KxInStockProduct::getInStockNum, bo.getInStockNum());
        lqw.eq(bo.getProductAttrId() != null, KxInStockProduct::getProductAttrId, bo.getProductAttrId());
        return lqw;
    }

    /**
     * 新增入库商品
     */
    @Override
    public Boolean insertByBo(KxInStockProductBo bo) {
        KxInStockProduct add = BeanUtil.toBean(bo, KxInStockProduct.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改入库商品
     */
    @Override
    public Boolean updateByBo(KxInStockProductBo bo) {
        KxInStockProduct update = BeanUtil.toBean(bo, KxInStockProduct.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxInStockProduct entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除入库商品
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
