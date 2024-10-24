package com.kxmall.web.controller.activity.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.activity.domain.KxStoreActivityProduct;
import com.kxmall.activity.domain.bo.KxStoreActivityProductBo;
import com.kxmall.activity.domain.vo.KxStoreActivityProductVo;
import com.kxmall.activity.mapper.KxStoreActivityProductMapper;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.product.domain.KxStoreProduct;
import com.kxmall.product.mapper.KxStoreProductMapper;
import com.kxmall.web.controller.activity.service.IKxStoreActivityProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 活动商品Service业务层处理
 *
 * @author kxmall
 * @date 2024-08-07
 */
@RequiredArgsConstructor
@Service
public class KxStoreActivityProductServiceImpl implements IKxStoreActivityProductService {

    private final KxStoreActivityProductMapper baseMapper;

    private final KxStoreProductMapper kxStoreProductMapper;


    /**
     * 查询活动商品
     */
    @Override
    public KxStoreActivityProductVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询活动商品列表
     */
    @Override
    public TableDataInfo<KxStoreActivityProductVo> queryPageList(KxStoreActivityProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStoreActivityProduct> lqw = buildQueryWrapper(bo);
        Page<KxStoreActivityProductVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询活动商品列表
     */
    @Override
    public List<KxStoreActivityProductVo> queryList(KxStoreActivityProductBo bo) {
        LambdaQueryWrapper<KxStoreActivityProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStoreActivityProduct> buildQueryWrapper(KxStoreActivityProductBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxStoreActivityProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, KxStoreActivityProduct::getProductId, bo.getProductId());
        lqw.eq(bo.getActivityId() != null, KxStoreActivityProduct::getActivityId, bo.getActivityId());
        return lqw;
    }

    /**
     * 新增活动商品
     */
    @Override
    public Boolean insertByBo(KxStoreActivityProductBo bo) {
        KxStoreActivityProduct add = BeanUtil.toBean(bo, KxStoreActivityProduct.class);
        validEntityBeforeSave(add);

        //获取当前商品价格
        KxStoreProduct kxStoreProduct = kxStoreProductMapper.selectById(bo.getProductId());
        bo.setActivityPrice(kxStoreProduct.getPrice());

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
    public Boolean updateByBo(KxStoreActivityProductBo bo) {
        KxStoreActivityProduct update = BeanUtil.toBean(bo, KxStoreActivityProduct.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStoreActivityProduct entity){
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

    @Override
    public Boolean addProductBatch(KxStoreActivityProductBo bo) {
        if (CollectionUtils.isEmpty(bo.getProductIds())) {
            throw new ServiceException("你要加入活动的商品不存在");
        }

        baseMapper.delete(new LambdaQueryWrapper<KxStoreActivityProduct>()
                .in(KxStoreActivityProduct::getProductId, bo.getProductIds())
                .eq(KxStoreActivityProduct::getActivityId, bo.getActivityId()));

        List<KxStoreActivityProduct> productDOList = new ArrayList<>();
        for (Long productId : bo.getProductIds()) {
            KxStoreActivityProduct add = new KxStoreActivityProduct();
            add.setProductId(productId);

            //获取当前商品价格
            KxStoreProduct kxStoreProduct = kxStoreProductMapper.selectById(productId);
            add.setActivityPrice(kxStoreProduct.getPrice());

            add.setActivityId(bo.getActivityId());
            Date now = new Date();
            add.setUpdateTime(now);
            add.setCreateTime(now);
            productDOList.add(add);
        }
        baseMapper.insertBatch(productDOList);
//        RedisUtils.deleteKeys(RECOMMEND_NAME + "*");
        return true;
    }

    @Override
    public Boolean updatePrice(KxStoreActivityProductBo bo) {
        KxStoreActivityProduct update = new KxStoreActivityProduct();
        update.setUpdateTime(new Date());
        update.setActivityPrice(bo.getActivityPrice());
        update.setId(bo.getId());
        return baseMapper.update(update, new LambdaQueryWrapper<KxStoreActivityProduct>().eq(KxStoreActivityProduct::getId, bo.getId())) > 0;
    }
}
