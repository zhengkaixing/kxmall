package com.kxmall.web.controller.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.user.domain.KxUserFootprint;
import com.kxmall.user.domain.bo.KxUserFootprintBo;
import com.kxmall.user.domain.vo.KxUserFootprintVo;
import com.kxmall.user.mapper.KxUserFootprintMapper;
import com.kxmall.web.controller.user.service.IKxUserFootprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 足迹Service业务层处理
 *
 * @author kxmall
 * @date 2023-04-06
 */
@RequiredArgsConstructor
@Service
public class KxUserFootprintService implements IKxUserFootprintService {

    private final KxUserFootprintMapper baseMapper;

    /**
     * 查询足迹
     */
    @Override
    public KxUserFootprintVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询足迹列表
     */
    @Override
    public TableDataInfo<KxUserFootprintVo> queryPageList(KxUserFootprintBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxUserFootprint> lqw = buildQueryWrapper(bo);
        Page<KxUserFootprintVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询足迹列表
     */
    @Override
    public List<KxUserFootprintVo> queryList(KxUserFootprintBo bo) {
        LambdaQueryWrapper<KxUserFootprint> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxUserFootprint> buildQueryWrapper(KxUserFootprintBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxUserFootprint> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, KxUserFootprint::getUserId, bo.getUserId());
        lqw.eq(bo.getProductId() != null, KxUserFootprint::getProductId, bo.getProductId());
        return lqw;
    }

    /**
     * 新增足迹
     */
    @Override
    public Boolean insertByBo(KxUserFootprintBo bo) {
        KxUserFootprint add = BeanUtil.toBean(bo, KxUserFootprint.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改足迹
     */
    @Override
    public Boolean updateByBo(KxUserFootprintBo bo) {
        KxUserFootprint update = BeanUtil.toBean(bo, KxUserFootprint.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxUserFootprint entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除足迹
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean addOrUpdateFootprint(Long userId, Long producId) {
        Date now = new Date();
        List<KxUserFootprint> footprintDOList = baseMapper.selectList(
            new QueryWrapper<KxUserFootprint>()
                .eq("user_id", userId)
                .eq("product_id", producId)
                .orderByDesc("update_time"));
        if (CollectionUtils.isEmpty(footprintDOList)) {
            KxUserFootprint footprintDO = new KxUserFootprint();
            footprintDO.setUserId(userId);
            footprintDO.setProductId(producId);
            footprintDO.setUpdateTime(now);
            footprintDO.setCreateTime(now);
            return baseMapper.insert(footprintDO) > 0;
        }
        KxUserFootprint footprintDO = footprintDOList.get(0);
        footprintDO.setUpdateTime(now);
        return baseMapper.updateById(footprintDO) > 0;
    }
}
