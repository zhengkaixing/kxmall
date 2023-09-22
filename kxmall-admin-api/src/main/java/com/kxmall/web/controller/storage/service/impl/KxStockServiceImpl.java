package com.kxmall.web.controller.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.product.domain.KxStoreCategory;
import com.kxmall.product.mapper.KxStoreCategoryMapper;
import com.kxmall.storage.domain.KxStock;
import com.kxmall.storage.domain.bo.KxStockBo;
import com.kxmall.storage.domain.bo.WarningStockBo;
import com.kxmall.storage.domain.vo.KxStockVo;
import com.kxmall.storage.mapper.KxStockMapper;
import com.kxmall.web.controller.storage.service.IKxStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 前置仓商品Service业务层处理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@RequiredArgsConstructor
@Service
public class KxStockServiceImpl implements IKxStockService {

    private final KxStockMapper baseMapper;

    private final KxStoreCategoryMapper categoryMapper;

    /**
     * 查询前置仓商品
     */
    @Override
    public KxStockVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询前置仓商品列表
     */
    @Override
    public TableDataInfo<KxStockVo> queryPageList(KxStockBo bo, PageQuery pageQuery) {
        //LambdaQueryWrapper<KxStock> lqw = buildQueryWrapper(bo);
        Integer offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        Integer size = pageQuery.getPageSize();
        List<KxStockVo> result = baseMapper.selectVoBySQL(offset,size,bo.getStorageId(),bo.getCategoryId()
            ,bo.getKeyword(),bo.getStatus(),bo.getNotIds());
        Long count = baseMapper.selectVoBySQLCount(bo.getStorageId(),bo.getCategoryId()
            ,bo.getKeyword(),bo.getStatus(),bo.getNotIds());
        return new TableDataInfo<KxStockVo>(result,count);
    }

    /**
     * 查询前置仓商品列表
     */
    @Override
    public List<KxStockVo> queryList(KxStockBo bo) {
        LambdaQueryWrapper<KxStock> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStock> buildQueryWrapper(KxStockBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxStock> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, KxStock::getProductId, bo.getProductId());
        lqw.eq(bo.getProductAttrId() != null, KxStock::getProductAttrId, bo.getProductAttrId());
        lqw.eq(bo.getStorageId() != null, KxStock::getStorageId, bo.getStorageId());
        lqw.eq(bo.getStatus() != null, KxStock::getStatus, bo.getStatus());
        lqw.eq(bo.getStock() != null, KxStock::getStock, bo.getStock());
        lqw.eq(bo.getSales() != null, KxStock::getSales, bo.getSales());
        lqw.eq(bo.getFrezzStock() != null, KxStock::getFrezzStock, bo.getFrezzStock());
        lqw.eq(bo.getPrice() != null, KxStock::getPrice, bo.getPrice());
        lqw.eq(bo.getWarningNum() != null, KxStock::getWarningNum, bo.getWarningNum());
        return lqw;
    }

    /**
     * 新增前置仓商品
     */
    @Override
    public Boolean insertByBo(KxStockBo bo) {
        KxStock add = BeanUtil.toBean(bo, KxStock.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改前置仓商品
     */
    @Override
    public Boolean updateByBo(KxStockBo bo) {
        KxStock update = BeanUtil.toBean(bo, KxStock.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStock entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除前置仓商品
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean freezeOrActivation(KxStockBo bo) {
        KxStock add = new KxStock();
        add.setStatus(bo.getStatus());
        add.setUpdateTime(new Date());
        add.setId(bo.getId());
        return baseMapper.update(add, new LambdaQueryWrapper<KxStock>().eq(KxStock::getId, bo.getId())) > 0;
    }

    @Override
    public Boolean updateByStock(KxStockBo bo) {
        return null;
    }

    @Override
    public Boolean updatePrice(KxStockBo bo) {
        KxStock update = new KxStock();
        update.setUpdateTime(new Date());
        update.setPrice(bo.getPrice());
        update.setId(bo.getId());
        return baseMapper.update(update, new LambdaQueryWrapper<KxStock>().eq(KxStock::getId, bo.getId())) > 0;
    }

    @Override
    public TableDataInfo<KxStockVo> queryPageWarningList(WarningStockBo bo, PageQuery pageQuery) {
        //类目查询
        LinkedList<Long> childrenIds = new LinkedList<>();
        if (bo.getCategoryId() != null && bo.getCategoryId() != 0L) {
            List<KxStoreCategory> childrenList = categoryMapper.selectList(new LambdaQueryWrapper<KxStoreCategory>().eq(KxStoreCategory::getPid, bo.getCategoryId()));
            if (!CollectionUtils.isEmpty(childrenList)) {
                //一级分类
                childrenList.forEach(item -> childrenIds.add(item.getId()));
                //使用in查询，就不需要等于查询
                bo.setCategoryId(null);
            }
        }
        Integer offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        Integer size = pageQuery.getPageSize();
        //查询制定页数的库存预警
        List<KxStockVo> result = baseMapper.warningListByStoragePage(offset,size, bo);
        Long count = baseMapper.warningListByStoragePageCount(bo);
        return new TableDataInfo<KxStockVo>(result,count);
    }

    @Override
    public Boolean warningUpdate(WarningStockBo bo) {
        KxStock update = new KxStock();
        update.setUpdateTime(new Date());
        update.setWarningNum(bo.getNum());
        return baseMapper.update(update, new LambdaQueryWrapper<KxStock>()
                                        .eq(KxStock::getStorageId, bo.getStorageId())
                                        .eq(KxStock::getProductId,bo.getProductId())
                                        .eq(KxStock::getProductAttrId,bo.getProductAttrId())) > 0;
    }


}
