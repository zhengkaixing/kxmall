package com.kxmall.web.controller.carousel.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.carousel.domain.KxCarousel;
import com.kxmall.carousel.domain.bo.KxCarouselBo;
import com.kxmall.carousel.domain.vo.KxCarouselVo;
import com.kxmall.carousel.mapper.KxCarouselMapper;
import com.kxmall.web.controller.carousel.service.IKxCarouselService;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.common.utils.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商铺广告Service业务层处理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@RequiredArgsConstructor
@Service
public class KxCarouselServiceImpl implements IKxCarouselService {

    private final KxCarouselMapper baseMapper;

    private final static String ADVERTISEMENT_NAME = "ADVERTISEMENT_TYPE_";

    /**
     * 查询商铺广告
     */
    @Override
    public KxCarouselVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商铺广告列表
     */
    @Override
    public TableDataInfo<KxCarouselVo> queryPageList(KxCarouselBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxCarousel> lqw = buildQueryWrapper(bo);
        Page<KxCarouselVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商铺广告列表
     */
    @Override
    public List<KxCarouselVo> queryList(KxCarouselBo bo) {
        LambdaQueryWrapper<KxCarousel> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxCarousel> buildQueryWrapper(KxCarouselBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxCarousel> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAdType() != null, KxCarousel::getAdType, bo.getAdType());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), KxCarousel::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getUrl()), KxCarousel::getUrl, bo.getUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getImgUrl()), KxCarousel::getImgUrl, bo.getImgUrl());
        lqw.eq(bo.getStatus() != null, KxCarousel::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getOutUrl()), KxCarousel::getOutUrl, bo.getOutUrl());
        return lqw;
    }

    /**
     * 新增商铺广告
     */
    @Override
    public Boolean insertByBo(KxCarouselBo bo) {
        Date now = new Date();
        boolean urlEmntyFlag = false;
        if (StringUtils.isEmpty(bo.getUrl()) && StringUtils.isEmpty(bo.getOutUrl())) {
            urlEmntyFlag = true;
        }
        if (urlEmntyFlag) {
            throw new ServiceException("广告URL不能为空");
        }
        KxCarousel add = BeanUtil.toBean(bo, KxCarousel.class);
        add.setCreateTime(now);
        add.setUpdateTime(now);
        if (baseMapper.insert(add) > 0) {
            RedisUtils.deleteKeys(ADVERTISEMENT_NAME + "*");
            return true;
        }
        throw new ServiceException("添加广告数据库失败");
    }

    /**
     * 修改商铺广告
     */
    @Override
    public Boolean updateByBo(KxCarouselBo bo) {
        Date now = new Date();
        boolean urlEmntyFlag = false;
        if (StringUtils.isEmpty(bo.getUrl()) && StringUtils.isEmpty(bo.getOutUrl())) {
            urlEmntyFlag = true;
        }
        if (urlEmntyFlag) {
            throw new ServiceException("广告URL不能为空");
        }
        KxCarousel update = BeanUtil.toBean(bo, KxCarousel.class);
        update.setUpdateTime(now);
        if (baseMapper.updateById(update) > 0) {
            RedisUtils.deleteKeys(ADVERTISEMENT_NAME + "*");
            return true;
        }
        throw new ServiceException("修改广告数据库失败");

    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxCarousel entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商铺广告
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        if (baseMapper.deleteBatchIds(ids) > 0) {
            RedisUtils.deleteKeys(ADVERTISEMENT_NAME + "*");
            return true;
        }
        throw new ServiceException("删除广告数据库失败");
    }
}
