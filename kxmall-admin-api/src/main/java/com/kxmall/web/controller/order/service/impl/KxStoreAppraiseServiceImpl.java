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
import com.kxmall.order.domain.bo.KxStoreAppraiseBo;
import com.kxmall.order.domain.vo.KxStoreAppraiseVo;
import com.kxmall.order.domain.KxStoreAppraise;
import com.kxmall.order.mapper.KxStoreAppraiseMapper;
import com.kxmall.web.controller.order.service.IKxStoreAppraiseService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 评论管理Service业务层处理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@RequiredArgsConstructor
@Service
public class KxStoreAppraiseServiceImpl implements IKxStoreAppraiseService {

    private final KxStoreAppraiseMapper baseMapper;

    /**
     * 查询评论管理
     */
    @Override
    public KxStoreAppraiseVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询评论管理列表
     */
    @Override
    public TableDataInfo<KxStoreAppraiseVo> queryPageList(KxStoreAppraiseBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStoreAppraise> lqw = buildQueryWrapper(bo);
        Page<KxStoreAppraiseVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询评论管理列表
     */
    @Override
    public List<KxStoreAppraiseVo> queryList(KxStoreAppraiseBo bo) {
        LambdaQueryWrapper<KxStoreAppraise> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStoreAppraise> buildQueryWrapper(KxStoreAppraiseBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxStoreAppraise> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, KxStoreAppraise::getProductId, bo.getProductId());
        lqw.eq(bo.getProductAttrId() != null, KxStoreAppraise::getProductAttrId, bo.getProductAttrId());
        lqw.eq(bo.getOrderId() != null, KxStoreAppraise::getOrderId, bo.getOrderId());
        lqw.eq(bo.getUserId() != null, KxStoreAppraise::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), KxStoreAppraise::getContent, bo.getContent());
        lqw.eq(bo.getScore() != null, KxStoreAppraise::getScore, bo.getScore());
        lqw.eq(bo.getState() != null, KxStoreAppraise::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增评论管理
     */
    @Override
    public Boolean insertByBo(KxStoreAppraiseBo bo) {
        KxStoreAppraise add = BeanUtil.toBean(bo, KxStoreAppraise.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改评论管理
     */
    @Override
    public Boolean updateByBo(KxStoreAppraiseBo bo) {
        KxStoreAppraise update = BeanUtil.toBean(bo, KxStoreAppraise.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStoreAppraise entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除评论管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
