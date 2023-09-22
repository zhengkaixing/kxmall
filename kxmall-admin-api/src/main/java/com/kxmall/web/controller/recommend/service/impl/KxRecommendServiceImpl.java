package com.kxmall.web.controller.recommend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.constant.UserConstants;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.domain.entity.SysUser;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.web.controller.product.service.IKxStoreProductService;
import com.kxmall.recommend.domain.KxRecommend;
import com.kxmall.recommend.domain.bo.KxRecommendBo;
import com.kxmall.recommend.domain.vo.KxRecommendVo;
import com.kxmall.recommend.mapper.KxRecommendMapper;
import com.kxmall.web.controller.recommend.service.IKxRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 推荐管理Service业务层处理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@RequiredArgsConstructor
@Service
public class KxRecommendServiceImpl implements IKxRecommendService {

    private final KxRecommendMapper baseMapper;

    private final IKxStoreProductService productService;

    private final static String RECOMMEND_NAME = "RECOMMEND_TYPE_";

    /**
     * 查询推荐管理
     */
    @Override
    public KxRecommendVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询推荐管理列表
     */
    @Override
    public TableDataInfo<KxRecommendVo> queryPageList(KxRecommendBo bo, PageQuery pageQuery) {
        Wrapper<KxRecommend> wrapper = buildQueryWrapperAlias(bo);
        Page<KxRecommendVo> result = baseMapper.selectVoPageBySQL(pageQuery.build(), wrapper);
        return TableDataInfo.build(result);
    }

    /**
     * 查询推荐管理列表
     */
    @Override
    public List<KxRecommendVo> queryList(KxRecommendBo bo) {
        LambdaQueryWrapper<KxRecommend> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxRecommend> buildQueryWrapper(KxRecommendBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxRecommend> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, KxRecommend::getProductId, bo.getProductId());
        lqw.eq(bo.getRecommendType() != null, KxRecommend::getRecommendType, bo.getRecommendType());
        return lqw;
    }
    private Wrapper<KxRecommend> buildQueryWrapperAlias(KxRecommendBo bo) {
        QueryWrapper<KxRecommend> wrapper = Wrappers.query();
        wrapper.eq(bo.getRecommendType() != null,"re.recommend_type", bo.getRecommendType());
        return wrapper;
    }

    /**
     * 新增推荐管理
     */
    @Override
    public Boolean insertByBo(KxRecommendBo bo) {
        if (productService.selectCountById(bo.getProductId()) > 0L) {
            throw new ServiceException("你要加入推荐的商品不存在");
        }

        if (baseMapper.selectCount(new LambdaQueryWrapper<KxRecommend>()
            .eq(KxRecommend::getProductId, bo.getProductId())
            .eq(KxRecommend::getRecommendType, bo.getRecommendType())) > 0) {
            throw new ServiceException("你要加入推荐的商品已推荐");
        }
        KxRecommend add = BeanUtil.toBean(bo, KxRecommend.class);
        Date now = new Date();
        add.setUpdateTime(now);
        add.setCreateTime(now);
        if (!(baseMapper.insert(add) > 0)) {
            throw new ServiceException("加入推荐数据库失败");
        }
        RedisUtils.deleteObject(RECOMMEND_NAME + bo.getRecommendType());
        return true;

    }

    /**
     * 修改推荐管理
     */
    @Override
    public Boolean updateByBo(KxRecommendBo bo) {
        KxRecommend update = BeanUtil.toBean(bo, KxRecommend.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxRecommend entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除推荐管理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        List<KxRecommend> recommends = baseMapper.selectList(new LambdaQueryWrapper<KxRecommend>().in(KxRecommend::getId, ids));
        for (KxRecommend recommend : recommends) {
            if (baseMapper.deleteById(recommend.getId()) > 0) {
                RedisUtils.deleteObject(RECOMMEND_NAME + recommend.getRecommendType());
            }
        }
        return true;
    }

    @Override
    public Boolean addRecommendBatch(KxRecommendBo bo) {

        if (CollectionUtils.isEmpty(bo.getProductIds())) {
            throw new ServiceException("你要加入推荐的商品不存在");
        }

        baseMapper.delete(new LambdaQueryWrapper<KxRecommend>()
            .in(KxRecommend::getProductId, bo.getProductIds())
            .eq(KxRecommend::getRecommendType, bo.getRecommendType()));

        List<KxRecommend> recommendDOList = new ArrayList<>();
        for (Long productId : bo.getProductIds()) {
            KxRecommend add = new KxRecommend();
            add.setProductId(productId);
            add.setRecommendType(bo.getRecommendType());
            Date now = new Date();
            add.setUpdateTime(now);
            add.setCreateTime(now);
            recommendDOList.add(add);
        }
        baseMapper.insertBatch(recommendDOList);
        RedisUtils.deleteObject(RECOMMEND_NAME+bo.getRecommendType());
        return true;
    }
}
