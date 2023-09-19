package com.kxmall.web.controller.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kxmall.order.domain.bo.KxStoreCartBo;
import com.kxmall.order.domain.vo.KxStoreCartVo;
import com.kxmall.order.domain.KxStoreCart;
import com.kxmall.order.mapper.KxStoreCartMapper;
import com.kxmall.web.controller.order.service.IKxStoreCartService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 购物车Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-15
 */
@RequiredArgsConstructor
@Service
public class KxStoreCartServiceImpl implements IKxStoreCartService {

    private final KxStoreCartMapper baseMapper;

    /**
     * 查询购物车
     */
    @Override
    public KxStoreCartVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询购物车列表
     */
    @Override
    public TableDataInfo<KxStoreCartVo> queryPageList(KxStoreCartBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStoreCart> lqw = buildQueryWrapper(bo);
        Page<KxStoreCartVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询购物车列表
     */
    @Override
    public List<KxStoreCartVo> queryList(KxStoreCartBo bo) {
        LambdaQueryWrapper<KxStoreCart> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStoreCart> buildQueryWrapper(KxStoreCartBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxStoreCart> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, KxStoreCart::getProductId, bo.getProductId());
        return lqw;
    }

    /**
     * 新增购物车
     */
    @Override
    public Boolean insertByBo(KxStoreCartBo bo) {
        KxStoreCart add = BeanUtil.toBean(bo, KxStoreCart.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改购物车
     */
    @Override
    public Boolean updateByBo(KxStoreCartBo bo) {
        KxStoreCart update = BeanUtil.toBean(bo, KxStoreCart.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStoreCart entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除购物车
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
