package com.kxmall.web.controller.seckill.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.seckill.domain.KxStoreSeckill;
import com.kxmall.seckill.domain.bo.KxStoreSeckillBo;
import com.kxmall.seckill.domain.vo.KxStoreSeckillVo;
import com.kxmall.seckill.mapper.KxStoreSeckillMapper;
import com.kxmall.web.controller.seckill.service.IKxStoreSeckillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 商品秒杀Service业务层处理
 *
 * @author kxmall
 * @date 2024-05-15
 */
@RequiredArgsConstructor
@Service
public class KxStoreSeckillServiceImpl implements IKxStoreSeckillService {

    private final KxStoreSeckillMapper baseMapper;

    /**
     * 查询商品秒杀
     */
    @Override
    public KxStoreSeckillVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品秒杀列表
     */
    @Override
    public TableDataInfo<KxStoreSeckillVo> queryPageList(KxStoreSeckillBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStoreSeckill> lqw = buildQueryWrapper(bo);
        Page<KxStoreSeckillVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品秒杀列表
     */
    @Override
    public List<KxStoreSeckillVo> queryList(KxStoreSeckillBo bo) {
        LambdaQueryWrapper<KxStoreSeckill> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStoreSeckill> buildQueryWrapper(KxStoreSeckillBo bo) {
        LambdaQueryWrapper<KxStoreSeckill> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, KxStoreSeckill::getProductId, bo.getProductId());
        lqw.eq(bo.getStorageId() != null, KxStoreSeckill::getStorageId, bo.getStorageId());
        lqw.eq(StringUtils.isNotBlank(bo.getImage()), KxStoreSeckill::getImage, bo.getImage());
        lqw.eq(StringUtils.isNotBlank(bo.getImages()), KxStoreSeckill::getImages, bo.getImages());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), KxStoreSeckill::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getInfo()), KxStoreSeckill::getInfo, bo.getInfo());
        lqw.eq(bo.getPrice() != null, KxStoreSeckill::getPrice, bo.getPrice());
        lqw.eq(bo.getCost() != null, KxStoreSeckill::getCost, bo.getCost());
        lqw.eq(bo.getOtPrice() != null, KxStoreSeckill::getOtPrice, bo.getOtPrice());
        lqw.eq(bo.getGiveIntegral() != null, KxStoreSeckill::getGiveIntegral, bo.getGiveIntegral());
        lqw.eq(bo.getSort() != null, KxStoreSeckill::getSort, bo.getSort());
        lqw.eq(bo.getStock() != null, KxStoreSeckill::getStock, bo.getStock());
        lqw.eq(bo.getSales() != null, KxStoreSeckill::getSales, bo.getSales());
        lqw.like(StringUtils.isNotBlank(bo.getUnitName()), KxStoreSeckill::getUnitName, bo.getUnitName());
        lqw.eq(bo.getPostage() != null, KxStoreSeckill::getPostage, bo.getPostage());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), KxStoreSeckill::getDescription, bo.getDescription());
        lqw.eq(bo.getStartTime() != null, KxStoreSeckill::getStartTime, bo.getStartTime());
        lqw.eq(bo.getStopTime() != null, KxStoreSeckill::getStopTime, bo.getStopTime());
        lqw.eq(bo.getStatus() != null, KxStoreSeckill::getStatus, bo.getStatus());
        lqw.eq(bo.getIsPostage() != null, KxStoreSeckill::getIsPostage, bo.getIsPostage());
        lqw.eq(bo.getIsHot() != null, KxStoreSeckill::getIsHot, bo.getIsHot());
        lqw.eq(bo.getNum() != null, KxStoreSeckill::getNum, bo.getNum());
        lqw.eq(bo.getIsShow() != null, KxStoreSeckill::getIsShow, bo.getIsShow());
        lqw.eq(bo.getTimeId() != null, KxStoreSeckill::getTimeId, bo.getTimeId());
        lqw.eq(bo.getSpecType() != null, KxStoreSeckill::getSpecType, bo.getSpecType());
        lqw.eq(bo.getTempId() != null, KxStoreSeckill::getTempId, bo.getTempId());
        lqw.eq(bo.getIsDel() != null, KxStoreSeckill::getIsDel, bo.getIsDel());
        return lqw;
    }

    /**
     * 新增商品秒杀
     */
    @Override
    public Boolean insertByBo(KxStoreSeckillBo bo) {
        KxStoreSeckill add = BeanUtil.toBean(bo, KxStoreSeckill.class);
        add.setPostage(BigDecimal.ZERO);
        validEntityBeforeSave(add);
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<KxStoreSeckill>().eq(KxStoreSeckill::getStorageId, bo.getStorageId()).eq(KxStoreSeckill::getProductId, bo.getProductId()));
        if (count !=0) {
            throw new ServiceException("该仓库下已存在该商品秒杀，无法再次新增");
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品秒杀
     */
    @Override
    public Boolean updateByBo(KxStoreSeckillBo bo) {
        KxStoreSeckill update = BeanUtil.toBean(bo, KxStoreSeckill.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStoreSeckill entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品秒杀
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
