package com.kxmall.web.controller.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.kxmall.product.domain.KxStoreProductAttr;
import com.kxmall.product.domain.KxStoreProductAttrResult;
import com.kxmall.product.domain.KxStoreProductAttrValue;
import com.kxmall.product.domain.bo.KxStoreProductAttrBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.product.domain.vo.FromatDetailVo;
import com.kxmall.product.domain.vo.ProductFormatVo;
import com.kxmall.product.mapper.KxStoreProductAttrResultMapper;
import com.kxmall.product.mapper.KxStoreProductAttrValueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kxmall.product.domain.vo.KxStoreProductAttrVo;
import com.kxmall.product.mapper.KxStoreProductAttrMapper;
import com.kxmall.web.controller.product.service.IKxStoreProductAttrService;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 商品属性Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-13
 */
@RequiredArgsConstructor
@Service
public class KxStoreProductAttrServiceImpl implements IKxStoreProductAttrService {

    private final KxStoreProductAttrMapper baseMapper;

    private final KxStoreProductAttrValueMapper storeProductAttrValueMapper;

    private final KxStoreProductAttrResultMapper storeProductAttrResultMapper;

    /**
     * 查询商品属性
     */
    @Override
    public KxStoreProductAttrVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品属性列表
     */
    @Override
    public TableDataInfo<KxStoreProductAttrVo> queryPageList(KxStoreProductAttrBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStoreProductAttr> lqw = buildQueryWrapper(bo);
        Page<KxStoreProductAttrVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品属性列表
     */
    @Override
    public List<KxStoreProductAttrVo> queryList(KxStoreProductAttrBo bo) {
        LambdaQueryWrapper<KxStoreProductAttr> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStoreProductAttr> buildQueryWrapper(KxStoreProductAttrBo bo) {
        LambdaQueryWrapper<KxStoreProductAttr> lqw = Wrappers.lambdaQuery();
        if (ObjectUtils.isEmpty(bo)) {
            return lqw;
        }
        Map<String, Object> params = bo.getParams();
        lqw.eq(bo.getProductId() != null, KxStoreProductAttr::getProductId, bo.getProductId());
        lqw.like(StringUtils.isNotBlank(bo.getAttrName()), KxStoreProductAttr::getAttrName, bo.getAttrName());
        lqw.eq(StringUtils.isNotBlank(bo.getAttrValues()), KxStoreProductAttr::getAttrValues, bo.getAttrValues());
        lqw.eq(bo.getIsDel() != null, KxStoreProductAttr::getIsDel, bo.getIsDel());
        return lqw;
    }

    /**
     * 新增商品属性
     */
    @Override
    public Boolean insertByBo(KxStoreProductAttrBo bo) {
        KxStoreProductAttr add = BeanUtil.toBean(bo, KxStoreProductAttr.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品属性
     */
    @Override
    public Boolean updateByBo(KxStoreProductAttrBo bo) {
        KxStoreProductAttr update = BeanUtil.toBean(bo, KxStoreProductAttr.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStoreProductAttr entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品属性
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public void insertYxStoreProductAttr(List<FromatDetailVo> items, List<ProductFormatVo> attrs, Long productId) {
        List<KxStoreProductAttr> attrGroup = new ArrayList<>();
        for (FromatDetailVo fromatDetailDto : items) {
            KxStoreProductAttr kxStoreProductAttr = KxStoreProductAttr.builder()
                .productId(productId)
                .attrName(fromatDetailDto.getValue())
                .attrValues(StrUtil.join(",",fromatDetailDto.getDetail()))
                .build();

            attrGroup.add(kxStoreProductAttr);
        }



        List<KxStoreProductAttrValue> valueGroup = new ArrayList<>();
        for (ProductFormatVo productFormatDto : attrs) {

            if(productFormatDto.getPinkStock()>productFormatDto.getStock() || productFormatDto.getSeckillStock()>productFormatDto.getStock()){
                throw new ServiceException("活动商品库存不能大于原有商品库存");
            }
            List<String> stringList = new ArrayList<>(productFormatDto.getDetail().values());
            Collections.sort(stringList);
            KxStoreProductAttrValue oldAttrValue = storeProductAttrValueMapper.selectOne(new LambdaQueryWrapper<KxStoreProductAttrValue>()
                .eq(KxStoreProductAttrValue::getSku, productFormatDto.getSku())
                .eq(KxStoreProductAttrValue::getProductId, productId));

            String unique = IdUtil.simpleUUID();
            if (Objects.nonNull(oldAttrValue)) {
                unique = oldAttrValue.getUnique();
            }

            KxStoreProductAttrValue yxStoreProductAttrValue = KxStoreProductAttrValue.builder()
                .id(Objects.isNull(oldAttrValue) ? null : oldAttrValue.getId())
                .productId(productId)
                .sku(StrUtil.join(",",stringList))
                .price(BigDecimal.valueOf(productFormatDto.getPrice()))
                .cost(BigDecimal.valueOf(productFormatDto.getCost()))
                .otPrice(BigDecimal.valueOf(productFormatDto.getOtPrice()))
                .unique(unique)
                .image(productFormatDto.getPic())
                .barCode(productFormatDto.getBarCode())
                .weight(BigDecimal.valueOf(productFormatDto.getWeight()))
                .volume(BigDecimal.valueOf(productFormatDto.getVolume()))
                .brokerage(BigDecimal.valueOf(productFormatDto.getBrokerage()))
                .brokerageTwo(BigDecimal.valueOf(productFormatDto.getBrokerageTwo()))
                .stock(productFormatDto.getStock())
                .integral(productFormatDto.getIntegral())
                .pinkPrice(BigDecimal.valueOf(productFormatDto.getPinkPrice()==null?0:productFormatDto.getPinkPrice()))
                .seckillPrice(BigDecimal.valueOf(productFormatDto.getSeckillPrice()==null?0:productFormatDto.getSeckillPrice()))
                .pinkStock(productFormatDto.getPinkStock()==null?0:productFormatDto.getPinkStock())
                .seckillStock(productFormatDto.getSeckillStock()==null?0:productFormatDto.getSeckillStock())
                .build();

            valueGroup.add(yxStoreProductAttrValue);
        }

        if(attrGroup.isEmpty() || valueGroup.isEmpty()){
            throw new ServiceException("请设置至少一个属性!");
        }

        //清理属性
        this.clearProductAttr(productId);

        //批量添加
        baseMapper.insertBatch(attrGroup);

        storeProductAttrValueMapper.insertBatch(valueGroup);


        Map<String,Object> map = new LinkedHashMap<>();
        map.put("attr",items);
        map.put("value",attrs);

        KxStoreProductAttrResult kxStoreProductAttrResult = new KxStoreProductAttrResult();
        kxStoreProductAttrResult.setProductId(productId);
        kxStoreProductAttrResult.setResult(JSON.toJSONString(map));
        kxStoreProductAttrResult.setChangeTime(new Date());

        long count = storeProductAttrResultMapper.selectCount(new LambdaQueryWrapper<KxStoreProductAttrResult>()
            .eq(KxStoreProductAttrResult::getProductId,productId));
        if(count > 0) {
            storeProductAttrResultMapper.delete(new LambdaQueryWrapper<KxStoreProductAttrResult>()
                .eq(KxStoreProductAttrResult::getProductId,productId));
        }
        storeProductAttrResultMapper.insertOrUpdate(kxStoreProductAttrResult);
    }

    @Override
    public List<KxStoreProductAttr> queryListAll() {
        return baseMapper.selectList();
    }

    /**
     * 删除YxStoreProductAttrValue表的属性
     * @param productId 商品id
     */
    private void clearProductAttr(Long productId) {
        if(ObjectUtil.isNull(productId)) {
            throw new ServiceException("产品不存在");
        }

        baseMapper.delete(Wrappers.<KxStoreProductAttr>lambdaQuery()
            .eq(KxStoreProductAttr::getProductId,productId));
        storeProductAttrValueMapper.delete(Wrappers.<KxStoreProductAttrValue>lambdaQuery()
            .eq(KxStoreProductAttrValue::getProductId,productId));

    }

}
