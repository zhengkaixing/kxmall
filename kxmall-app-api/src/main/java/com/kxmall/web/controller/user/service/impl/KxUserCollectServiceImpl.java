package com.kxmall.web.controller.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.storage.domain.vo.KxStockVo;
import com.kxmall.user.domain.KxUserCollect;
import com.kxmall.user.domain.bo.KxUserCollectBo;
import com.kxmall.user.domain.vo.KxUserCollectVo;
import com.kxmall.user.mapper.KxUserCollectMapper;
import com.kxmall.web.controller.user.service.IKxUserCollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 客户收藏Service业务层处理
 *
 * @author kxmall
 * @date 2023-04-06
 */
@RequiredArgsConstructor
@Service
public class KxUserCollectServiceImpl implements IKxUserCollectService {

    private final KxUserCollectMapper baseMapper;

    /**
     * 查询客户收藏
     */
    @Override
    public KxUserCollectVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询客户收藏列表
     */
    @Override
    public TableDataInfo<KxUserCollectVo> queryPageList(KxUserCollectBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxUserCollect> lqw = buildQueryWrapper(bo);
        Page<KxUserCollectVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询客户收藏列表
     */
    @Override
    public List<KxUserCollectVo> queryList(KxUserCollectBo bo) {
        LambdaQueryWrapper<KxUserCollect> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxUserCollect> buildQueryWrapper(KxUserCollectBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxUserCollect> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, KxUserCollect::getUserId, bo.getUserId());
        lqw.eq(bo.getProductId() != null, KxUserCollect::getProductId, bo.getProductId());
        return lqw;
    }

    /**
     * 新增客户收藏
     */
    @Override
    public Boolean insertByBo(KxUserCollectBo bo) {
        KxUserCollect add = BeanUtil.toBean(bo, KxUserCollect.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改客户收藏
     */
    @Override
    public Boolean updateByBo(KxUserCollectBo bo) {
        KxUserCollect update = BeanUtil.toBean(bo, KxUserCollect.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxUserCollect entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除客户收藏
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public int deleteCollect(KxUserCollectBo bo) {
        return baseMapper.delete(new LambdaQueryWrapper<KxUserCollect>()
                    .eq(KxUserCollect::getUserId, bo.getUserId())
                    .eq(KxUserCollect::getProductId, bo.getProductId()));
    }


    @Override
    public TableDataInfo<KxUserCollectVo> getCollectAll(KxUserCollectBo bo, PageQuery pageQuery) {
        Integer offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        Integer size = pageQuery.getPageSize();

        List<KxUserCollectVo> collectAll = baseMapper.getCollectAll(bo.getUserId(), offset, size);
        Long count = baseMapper.getCollectAllByCount(bo.getUserId());
        return new TableDataInfo<>(collectAll, count);
    }
}
