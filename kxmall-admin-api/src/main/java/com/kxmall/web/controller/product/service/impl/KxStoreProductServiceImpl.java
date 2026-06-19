package com.kxmall.web.controller.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.ShopCommonEnum;
import com.kxmall.common.enums.SpecTypeEnum;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.GeneratorUtil;
import com.kxmall.common.utils.RegexUtil;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.product.domain.KxStoreCategory;
import com.kxmall.product.domain.KxStoreProduct;
import com.kxmall.product.domain.KxStoreProductAttrResult;
import com.kxmall.product.domain.KxStoreProductAttrValue;
import com.kxmall.product.domain.bo.KxStoreProductBo;
import com.kxmall.product.domain.vo.*;
import com.kxmall.product.mapper.KxStoreCategoryMapper;
import com.kxmall.product.mapper.KxStoreProductAttrResultMapper;
import com.kxmall.product.mapper.KxStoreProductAttrValueMapper;
import com.kxmall.product.mapper.KxStoreProductMapper;
import com.kxmall.storage.domain.KxStock;
import com.kxmall.storage.domain.vo.KxStockVo;
import com.kxmall.storage.mapper.KxStockMapper;
import com.kxmall.web.controller.product.service.IKxStoreProductAttrService;
import com.kxmall.web.controller.product.service.IKxStoreProductRuleService;
import com.kxmall.web.controller.product.service.IKxStoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-13
 */
@RequiredArgsConstructor
@Service
public class KxStoreProductServiceImpl implements IKxStoreProductService {

    private final KxStoreProductMapper baseMapper;

    private final KxStoreCategoryMapper storeCategoryMapper;

    private final IKxStoreProductRuleService kxStoreProductRuleService;

    private final IKxStoreProductAttrService kxStoreProductAttrService;

    private final KxStoreProductAttrValueMapper kxStoreProductAttrValueMapper;

    private final KxStoreProductAttrResultMapper kxStoreProductAttrResultMapper;

    private final KxStockMapper kxStockMapper;

    private final KxStoreCategoryMapper categoryMapper;

    private static final String RECOMMEND_NAME = "RECOMMEND_TYPE_";

    /**
     * 查询商品
     */
    @Override
    public Map<String, Object> queryById(Long id) {
        Map<String, Object> map = new LinkedHashMap<>(3);


        //商品规格
        map.put("ruleList", kxStoreProductRuleService.queryListAll());


        if (id == 0) {
            return map;
        }

        //处理商品详情
        KxStoreProduct kxStoreProduct = baseMapper.selectById(id);
        ProductVo productVo = new ProductVo();
        BeanUtil.copyProperties(kxStoreProduct, productVo, "sliderImage");
        productVo.setImage(JSONArray.parseArray(kxStoreProduct.getImage()));
        productVo.setSliderImage(JSONArray.parseArray(kxStoreProduct.getSliderImage()));
        KxStoreProductAttrResult storeProductAttrResult = kxStoreProductAttrResultMapper
                .selectOne(new LambdaQueryWrapper<KxStoreProductAttrResult>()
                        .eq(KxStoreProductAttrResult::getProductId, id).last("limit 1"));
        JSONObject result = JSON.parseObject(storeProductAttrResult.getResult());
        List<KxStoreProductAttrValue> attrValues = kxStoreProductAttrValueMapper.selectList(new LambdaQueryWrapper<KxStoreProductAttrValue>().eq(KxStoreProductAttrValue::getProductId, kxStoreProduct.getId()));
        List<ProductFormatVo> productFormatDtos = attrValues.stream().map(i -> {
            ProductFormatVo productFormatDto = new ProductFormatVo();
            BeanUtils.copyProperties(i, productFormatDto);
            productFormatDto.setPic(i.getImage());
            return productFormatDto;
        }).collect(Collectors.toList());
        if (SpecTypeEnum.TYPE_1.getValue().equals(kxStoreProduct.getSpecType())) {
            productVo.setAttr(new ProductFormatVo());
            productVo.setAttrs(productFormatDtos);
            productVo.setItems(result.getObject("attr", ArrayList.class));
        } else {

            productFromat(productVo, result);
        }

        map.put("productInfo", productVo);

        return map;
    }


    /**
     * 获取商品属性
     *
     * @param productDto
     * @param result
     */
    private void productFromat(ProductVo productDto, JSONObject result) {
        Map<String, Object> mapAttr = (Map<String, Object>) result.getObject("value", ArrayList.class).get(0);
        ProductFormatVo productFormatDto = ProductFormatVo.builder()
                .pic(mapAttr.get("pic").toString())
                .price(Double.valueOf(mapAttr.get("price").toString()))
                .cost(Double.valueOf(mapAttr.get("cost").toString()))
                .otPrice(Double.valueOf(mapAttr.get("otPrice").toString()))
                .stock(Long.valueOf(mapAttr.get("stock").toString()))
                .barCode(mapAttr.get("barCode").toString())
                .weight(Double.valueOf(mapAttr.get("weight").toString()))
                .volume(Double.valueOf(mapAttr.get("volume").toString()))
                .value1(mapAttr.get("value1").toString())
                .integral(mapAttr.get("integral") != null ? Long.parseLong(mapAttr.get("integral").toString()) : 0)
                .brokerage(Double.valueOf(mapAttr.get("brokerage").toString()))
                .brokerageTwo(Double.valueOf(mapAttr.get("brokerageTwo").toString()))
                .pinkPrice(Double.valueOf(mapAttr.get("pinkPrice").toString()))
                .pinkStock(Integer.valueOf(mapAttr.get("pinkStock").toString()))
                .seckillPrice(Double.valueOf(mapAttr.get("seckillPrice").toString()))
                .seckillStock(Integer.valueOf(mapAttr.get("seckillStock").toString()))
                .build();
        productDto.setAttr(productFormatDto);
    }


    /**
     * 查询商品列表
     */
    @Override
    public TableDataInfo<KxStoreProductVo> queryPageList(KxStoreProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStoreProduct> lqw = buildQueryWrapper(bo);
        Page<KxStoreProductVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<KxStoreProductVo> records = result.getRecords();
        records.forEach(kxStoreProductVo -> {
            kxStoreProductVo.setStoreCategory(storeCategoryMapper.selectVoById(kxStoreProductVo.getCateId()));
        });
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品列表
     */
    @Override
    public List<KxStoreProductVo> queryList(KxStoreProductBo bo) {
        LambdaQueryWrapper<KxStoreProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStoreProduct> buildQueryWrapper(KxStoreProductBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxStoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getMerId() != null, KxStoreProduct::getMerId, bo.getMerId());
        lqw.eq(CollectionUtils.isNotEmpty(bo.getImage()), KxStoreProduct::getImage, bo.getImage());
        lqw.eq(CollectionUtils.isNotEmpty(bo.getSliderImage()), KxStoreProduct::getSliderImage, bo.getSliderImage());
        // Add keyword search when storeName is provided
        if (StringUtils.isNotBlank(bo.getStoreName())) {
            String storeName = bo.getStoreName();
            lqw.and(wrapper -> wrapper
                    .like(KxStoreProduct::getStoreName, storeName)
                    .or()
                    .like(KxStoreProduct::getKeyword, storeName)
                    .or()
                    .apply("REPLACE(REPLACE(keyword, '，', ','), ' ', '') LIKE CONCAT('%', REPLACE(REPLACE({0}, '，', ','), ' ', ''), '%')", storeName)
            );
        }
        lqw.eq(StringUtils.isNotBlank(bo.getStoreInfo()), KxStoreProduct::getStoreInfo, bo.getStoreInfo());
        lqw.eq(StringUtils.isNotBlank(bo.getKeyword()), KxStoreProduct::getKeyword, bo.getKeyword());
        lqw.eq(StringUtils.isNotBlank(bo.getBarCode()), KxStoreProduct::getBarCode, bo.getBarCode());
        lqw.eq(bo.getCateId() != null, KxStoreProduct::getCateId, bo.getCateId());
        lqw.eq(bo.getPrice() != null, KxStoreProduct::getPrice, bo.getPrice());
        lqw.eq(bo.getVipPrice() != null, KxStoreProduct::getVipPrice, bo.getVipPrice());
        lqw.eq(bo.getOtPrice() != null, KxStoreProduct::getOtPrice, bo.getOtPrice());
        lqw.eq(bo.getPostage() != null, KxStoreProduct::getPostage, bo.getPostage());
        lqw.like(StringUtils.isNotBlank(bo.getUnitName()), KxStoreProduct::getUnitName, bo.getUnitName());
        lqw.eq(bo.getSort() != null, KxStoreProduct::getSort, bo.getSort());
        lqw.eq(bo.getSales() != null, KxStoreProduct::getSales, bo.getSales());
        lqw.eq(bo.getStock() != null, KxStoreProduct::getStock, bo.getStock());
        lqw.eq(bo.getIsShow() != null, KxStoreProduct::getIsShow, bo.getIsShow());
        lqw.eq(bo.getIsHot() != null, KxStoreProduct::getIsHot, bo.getIsHot());
        lqw.eq(bo.getIsBenefit() != null, KxStoreProduct::getIsBenefit, bo.getIsBenefit());
        lqw.eq(bo.getIsBest() != null, KxStoreProduct::getIsBest, bo.getIsBest());
        lqw.eq(bo.getIsNew() != null, KxStoreProduct::getIsNew, bo.getIsNew());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), KxStoreProduct::getDescription, bo.getDescription());
        lqw.eq(bo.getIsPostage() != null, KxStoreProduct::getIsPostage, bo.getIsPostage());
        lqw.eq(bo.getIsDel() != null, KxStoreProduct::getIsDel, bo.getIsDel());
        lqw.eq(bo.getMerUse() != null, KxStoreProduct::getMerUse, bo.getMerUse());
        lqw.eq(bo.getGiveIntegral() != null, KxStoreProduct::getGiveIntegral, bo.getGiveIntegral());
        lqw.eq(bo.getCost() != null, KxStoreProduct::getCost, bo.getCost());
        lqw.eq(bo.getIsSeckill() != null, KxStoreProduct::getIsSeckill, bo.getIsSeckill());
        lqw.eq(bo.getIsBargain() != null, KxStoreProduct::getIsBargain, bo.getIsBargain());
        lqw.eq(bo.getIsGood() != null, KxStoreProduct::getIsGood, bo.getIsGood());
        lqw.eq(bo.getFicti() != null, KxStoreProduct::getFicti, bo.getFicti());
        lqw.eq(bo.getBrowse() != null, KxStoreProduct::getBrowse, bo.getBrowse());
        lqw.eq(StringUtils.isNotBlank(bo.getCodePath()), KxStoreProduct::getCodePath, bo.getCodePath());
        lqw.eq(bo.getIsSub() != null, KxStoreProduct::getIsSub, bo.getIsSub());
        lqw.eq(bo.getTempId() != null, KxStoreProduct::getTempId, bo.getTempId());
        lqw.eq(bo.getSpecType() != null, KxStoreProduct::getSpecType, bo.getSpecType());
        lqw.eq(bo.getIsIntegral() != null, KxStoreProduct::getIsIntegral, bo.getIsIntegral());
        lqw.eq(bo.getIntegral() != null, KxStoreProduct::getIntegral, bo.getIntegral());
        return lqw;
    }

    /**
     * 新增商品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertAndupdateByBo(KxStoreProductBo bo) {
        //将商品编码拷贝到商品列表
        bo.setBarCode(bo.getAttrs().get(0).getBarCode());
        if (StringUtils.isNotEmpty(bo.getDescription())) {
            bo.setDescription(RegexUtil.converProductDescription(bo.getDescription()));
        }
        //如果是多规格，预先生成一个公共的ID，用于关联
        String productCommonId = null;
        if (SpecTypeEnum.TYPE_1.getValue().equals(bo.getSpecType())) {
            if (StringUtils.isNotEmpty(bo.getCommonId())) {
                productCommonId = bo.getCommonId();
            }else {
                productCommonId = GeneratorUtil.genOrderId();
            }
        }
        for (int i = 0; i < bo.getAttrs().size(); i++) {
            ProductResultVo resultDTO = this.computedProduct(bo.getAttrs().get(i));
            // 一级和二级分类均可
            Long count = categoryMapper.selectCount(new LambdaQueryWrapper<KxStoreCategory>().eq(KxStoreCategory::getId, bo.getCateId()));
            if (count == 0L) {
                throw new ServiceException("请选择商品分类");
            }
            //添加商品
            KxStoreProduct kxStoreProduct = new KxStoreProduct();
            BeanUtil.copyProperties(bo, kxStoreProduct, "sliderImage");
            if (bo.getSliderImage().isEmpty()) {
                throw new ServiceException("请上传轮播图");
            }
            if (ObjectUtils.isEmpty(bo.getImage())) {
                throw new ServiceException("请上传商品封面图");
            }
            if (bo.getAttrs().size() != 1) {
                StringBuilder name = new StringBuilder();
                // 先检查detail是否为空
                if (bo.getAttrs().get(i).getDetail() != null) {
                    // 遍历并打印所有value
                    for (Map.Entry<String, String> stringEntry : bo.getAttrs().get(i).getDetail().entrySet()) {
                        name.append(stringEntry.getValue());
                    }
                }
                // 名称需要单独处理下
                kxStoreProduct.setStoreName(kxStoreProduct.getStoreName() + name);
            }

            kxStoreProduct.setPrice(BigDecimal.valueOf(resultDTO.getMinPrice()));
            kxStoreProduct.setOtPrice(BigDecimal.valueOf(resultDTO.getMinOtPrice()));
            kxStoreProduct.setCost(BigDecimal.valueOf(resultDTO.getMinCost()));
            kxStoreProduct.setIntegral(resultDTO.getMinIntegral());
            kxStoreProduct.setStock(resultDTO.getStock());
            kxStoreProduct.setSliderImage(JSONArray.toJSONString(bo.getSliderImage()));
            if (SpecTypeEnum.TYPE_1.getValue().equals(bo.getSpecType())) {
                kxStoreProduct.setCommonId(productCommonId);
            }
            baseMapper.insertOrUpdate(kxStoreProduct);


            //处理单sKu
            FromatDetailVo fromatDetailDto = FromatDetailVo.builder()
                    .value("规格")
                    .detailValue("")
                    .attrHidden("")
                    .detail(ListUtil.toList("默认"))
                    .build();
            List<ProductFormatVo> attrs = bo.getAttrs();
            ProductFormatVo productFormatDto = attrs.get(i);
            productFormatDto.setValue1("规格");
            Map<String, String> map = new HashMap<>();
            map.put("规格", "默认");
            productFormatDto.setDetail(map);
            if (ObjectUtils.isEmpty(productFormatDto.getProductId())) {
                productFormatDto.setProductId(kxStoreProduct.getId());
            }
            kxStoreProductAttrService.insertKxStoreProductAttr(ListUtil.toList(fromatDetailDto),
                    ListUtil.toList(productFormatDto), productFormatDto.getProductId());

            RedisUtils.deleteKeys(RECOMMEND_NAME + "*");
        }
        return true;
    }

    /**
     * 计算产品数据
     *
     * @param attrs attrs
     * @return ProductResultVo
     */
    private ProductResultVo computedProduct(ProductFormatVo attrs) {
        // 获取价格，若属性为 null 则返回 0
        Double minPrice = (attrs.getPrice() != null) ? attrs.getPrice() : 0d;

        // 获取积分，若属性为 null 则返回 0
        long minIntegral = (attrs.getIntegral() != null) ? attrs.getIntegral() : 0L;

        // 获取其他价格，若属性为 null 则返回 0
        Double minOtPrice = (attrs.getOtPrice() != null) ? attrs.getOtPrice() : 0d;

        // 获取成本，若属性为 null 则返回 0
        Double minCost = (attrs.getCost() != null) ? attrs.getCost() : 0d;

        // 获取库存，若属性为 null 则返回 0
        long stock = (attrs.getStock() != null) ? attrs.getStock() : 0L;

        if (stock <= 0L) {
            throw new ServiceException("库存不能低于0");
        }

        return ProductResultVo.builder()
                .minPrice(minPrice)
                .minOtPrice(minOtPrice)
                .minCost(minCost)
                .stock(stock)
                .minIntegral(minIntegral)
                .build();
    }


    /**
     * 修改商品
     */
    @Override
    public Boolean updateByBo(KxStoreProductBo bo) {
        KxStoreProduct update = BeanUtil.toBean(bo, KxStoreProduct.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStoreProduct entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            Long count = kxStockMapper.selectCount(new LambdaQueryWrapper<KxStock>().in(KxStock::getProductId, ids));
            if (count > 0L) {
                throw new ServiceException("该商品存在授权仓库，无法删除！请先下架和删除对应的前置仓商品。");
            }
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Long selectCountByCateId(Long cateId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<KxStoreProduct>()
                .eq(KxStoreProduct::getCateId, cateId));
    }

    @Override
    public void onSale(Long id, Integer status) {
        if (ShopCommonEnum.SHOW_1.getValue().equals(status)) {
            status = ShopCommonEnum.SHOW_1.getValue();
        } else {
            status = ShopCommonEnum.SHOW_0.getValue();
        }
        KxStoreProduct update = new KxStoreProduct();
        update.setId(id);
        update.setIsShow(status);
        baseMapper.updateById(update);
    }

    /**
     * 获取生成的属性
     *
     * @param id
     * @param jsonStr
     * @param isActivity
     * @return
     */
    @Override
    public Map<String, Object> getFormatAttr(Long id, String jsonStr, boolean isActivity) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Map<String, Object> resultMap = new LinkedHashMap<>(3);

        if (jsonObject == null || jsonObject.get("attrs") == null || jsonObject.getJSONArray("attrs").isEmpty()) {
            resultMap.put("attr", new ArrayList<>());
            resultMap.put("value", new ArrayList<>());
            resultMap.put("header", new ArrayList<>());
            return resultMap;
        }

        List<FromatDetailVo> fromatDetailDTOList = JSON.parseArray(jsonObject.get("attrs").toString(),
                FromatDetailVo.class);

        //fromatDetailDTOList
        DetailVo detailVo = this.attrFormat(fromatDetailDTOList);

        List<Map<String, Object>> headerMapList = null;
        List<Map<String, Object>> valueMapList = new ArrayList<>();
        String align = "center";
        Map<String, Object> headerMap = new LinkedHashMap<>();

        // 1. 如果id>0，先查询当前商品的commonId
        String commonId = null;
        if (id > 0) {
            KxStoreProduct product = baseMapper.selectById(id);
            if (product != null && StringUtils.isNotEmpty(product.getCommonId())) {
                commonId = product.getCommonId();
            }
        }

        for (Map<String, Map<String, String>> map : detailVo.getRes()) {
            Map<String, String> detail = map.get("detail");
            String[] detailArr = detail.values().toArray(new String[]{});
            Arrays.sort(detailArr);

            String sku = String.join(",", detailArr);

            Map<String, Object> valueMap = new LinkedHashMap<>();

            List<String> detailKeys =
                    detail.entrySet()
                            .stream()
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());

            int i = 0;
            headerMapList = new ArrayList<>();
            for (String title : detailKeys) {
                headerMap.put("title", title);
                headerMap.put("minWidth", "130");
                headerMap.put("align", align);
                headerMap.put("key", "value" + (i + 1));
                headerMap.put("slot", "value" + (i + 1));
                headerMapList.add(ObjectUtil.clone(headerMap));
                i++;
            }

            String[] detailValues = detail.values().toArray(new String[]{});
            for (int j = 0; j < detailValues.length; j++) {
                String key = "value" + (j + 1);
                valueMap.put(key, detailValues[j]);
            }

            valueMap.put("detail", detail);
            valueMap.put("sku", sku);
            valueMap.put("pic", "");
            valueMap.put("price", 0);
            valueMap.put("cost", 0);
            valueMap.put("otPrice", 0);
            valueMap.put("stock", 0);
            valueMap.put("barCode", "");
            valueMap.put("weight", 0);
            valueMap.put("volume", 0);
            valueMap.put("brokerage", 0);
            valueMap.put("brokerageTwo", 0);
            valueMap.put("pinkPrice", 0);
            valueMap.put("seckillPrice", 0);
            valueMap.put("pinkStock", 0);
            valueMap.put("seckillStock", 0);
            valueMap.put("integral", 0);

            // 2. 查询当前商品和所有相关商品的属性值
            if (id > 0) {
                List<Long> productIds = new ArrayList<>();
                productIds.add(id);

                // 如果有commonId，查询所有相关商品
                if (commonId != null) {
                    List<KxStoreProduct> relatedProducts = baseMapper.selectList(
                            new LambdaQueryWrapper<KxStoreProduct>()
                                    .eq(KxStoreProduct::getCommonId, commonId)
                                    .ne(KxStoreProduct::getId, id)); // 排除当前商品

                    if (!relatedProducts.isEmpty()) {
                        productIds.addAll(relatedProducts.stream()
                                .map(KxStoreProduct::getId)
                                .collect(Collectors.toList()));
                    }
                }

                // 查询所有相关商品的属性值
                List<KxStoreProductAttrValue> storeProductAttrValueList = kxStoreProductAttrValueMapper
                        .selectList(new LambdaQueryWrapper<KxStoreProductAttrValue>()
                                .in(KxStoreProductAttrValue::getProductId, productIds)
                                .eq(KxStoreProductAttrValue::getSku, sku)
                                .orderByDesc(KxStoreProductAttrValue::getProductId));
                for (KxStoreProductAttrValue storeProductAttrValue : storeProductAttrValueList) {
                    Map<String, Object> Temp = new LinkedHashMap<>();
                    Temp.put("sku", storeProductAttrValue.getSku());
                    Temp.put("pic", storeProductAttrValue.getImage());
                    Temp.put("price", storeProductAttrValue.getPrice());
                    Temp.put("cost", storeProductAttrValue.getCost());
                    Temp.put("otPrice", storeProductAttrValue.getOtPrice());
                    Temp.put("stock", storeProductAttrValue.getStock());
                    Temp.put("barCode", storeProductAttrValue.getBarCode());
                    Temp.put("weight", storeProductAttrValue.getWeight());
                    Temp.put("volume", storeProductAttrValue.getVolume());
                    Temp.put("brokerage", storeProductAttrValue.getBrokerage());
                    Temp.put("brokerageTwo", storeProductAttrValue.getBrokerageTwo());
                    Temp.put("pinkPrice", storeProductAttrValue.getPinkPrice());
                    Temp.put("seckillPrice", storeProductAttrValue.getSeckillPrice());
                    Temp.put("pinkStock", storeProductAttrValue.getPinkStock());
                    Temp.put("seckillStock", storeProductAttrValue.getSeckillStock());
                    Temp.put("integral", storeProductAttrValue.getIntegral());
                    Temp.put("productId", storeProductAttrValue.getProductId());
                    valueMapList.add(ObjectUtil.clone(Temp));
                }
            }else {
                valueMapList.add(ObjectUtil.clone(valueMap));
            }
        }

        this.addMap(headerMap, headerMapList, align, isActivity);

        resultMap.put("attr", fromatDetailDTOList);
        resultMap.put("value", valueMapList);
        resultMap.put("header", headerMapList);

        return resultMap;
    }

    @Override
    public Long selectCountById(Long productId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<KxStoreProduct>().eq(KxStoreProduct::getId, productId));
    }

    @Override
    public Boolean batchAuthorizeGoods(KxStoreProductBo bo) {

        if (CollectionUtils.isEmpty(bo.getIds())) {
            throw new ServiceException("商品规格不存在!");
        }
        List<KxStock> list = new ArrayList<>();
        bo.getIds().stream().forEach(id -> {
            KxStoreProduct storeProducts = baseMapper.selectOne(new LambdaQueryWrapper<KxStoreProduct>().eq(KxStoreProduct::getId, id));
            KxStock kxStock = new KxStock();
            kxStock.setPrice(storeProducts.getPrice());
            kxStock.setProductId(id);
            kxStock.setProductAttrId(id);
            kxStock.setStorageId(bo.getStorageId());
            kxStock.setStock(storeProducts.getStock());
            kxStock.setStatus(1);
            list.add(kxStock);
        });
        //错误数据
        for (KxStock kxStock : list) {
            Long productId = kxStock.getProductId();
            Long storageId = kxStock.getStorageId();
            //判断此仓库中是否有该商品
            List<KxStock> stockList = kxStockMapper.selectList(new LambdaQueryWrapper<KxStock>()
                    .eq(KxStock::getProductId, productId)
                    .eq(KxStock::getStorageId, storageId));
            if (CollectionUtils.isEmpty(stockList)) {
                KxStock stockdo = new KxStock();
                stockdo.setProductId(productId);
                stockdo.setStorageId(storageId);
                stockdo.setProductAttrId(productId);
                Integer status = kxStock.getStatus();
                BigDecimal price = kxStock.getPrice();
                Long stock = kxStock.getStock();
                if (status != null && price != null && stock != null) {
                    stockdo.setStatus(status);
                    stockdo.setPrice(price);
                    stockdo.setStock(kxStock.getStock());
                    stockdo.setFrezzStock(0L);
                    stockdo.setSales(0L);
                    Date now = new Date();
                    stockdo.setCreateTime(now);
                    stockdo.setUpdateTime(now);
                    kxStockMapper.insert(stockdo);
                } else {
                    throw new ServiceException("信息不全!", 500);
                }
            }
        }
        return true;
    }

    @Override
    public List<ProductTreeNodeVo> getProductBigTree() {
        List<KxStoreCategory> storeCategoryList = storeCategoryMapper.selectList(new LambdaQueryWrapper<KxStoreCategory>().orderByAsc(KxStoreCategory::getPid));
        if (storeCategoryList != null && storeCategoryList.size() > 0) {
            Integer recordLevelOne = 0;
            List<KxStoreProduct> storeProductList = baseMapper.getProductTitleAll();
            List<ProductTreeNodeVo> list = new ArrayList<>();
            for (KxStoreCategory categoryDO : storeCategoryList) {
                if (categoryDO.getPid() == 0L) {
                    recordLevelOne++;
                }
            }
            Integer recordLevelTwo = storeCategoryList.size();
            for (int i = 0; i < recordLevelOne; i++) {
                KxStoreCategory categoryOnI = storeCategoryList.get(i);    //一级类目
                ProductTreeNodeVo dtoOnI = new ProductTreeNodeVo();
                dtoOnI.setLabel(categoryOnI.getCateName());
                dtoOnI.setValue("C_" + categoryOnI.getId());
                dtoOnI.setId(categoryOnI.getId());
                dtoOnI.setChildren(new LinkedList<>());
                for (int j = recordLevelOne; j < recordLevelTwo; j++) {
                    KxStoreCategory categoryOnJ = storeCategoryList.get(j);    //二级类目
                    if (!categoryOnJ.getPid().equals(dtoOnI.getId())) {
                        continue;
                    }
                    ProductTreeNodeVo dtoOnJ = new ProductTreeNodeVo();
                    dtoOnJ.setLabel(categoryOnJ.getCateName());
                    dtoOnJ.setValue("C_" + categoryOnJ.getId());
                    dtoOnJ.setId(categoryOnJ.getId());
                    dtoOnJ.setChildren(new LinkedList<>());
                    for (int k = 0; k < storeProductList.size(); k++) {
                        if (k != 0 && storeProductList.get(k - 1).getCateId().equals(dtoOnJ.getId()) && !storeProductList.get(k).getCateId().equals(dtoOnJ.getId())) {
                            break;
                        }
                        KxStoreProduct storeProduct = storeProductList.get(k);        //商品
                        if (storeProduct.getCateId().equals(dtoOnJ.getId())) {
                            ProductTreeNodeVo dtoOnK = new ProductTreeNodeVo();
                            dtoOnK.setLabel(storeProduct.getStoreName());
                            dtoOnK.setValue("G_" + storeProduct.getId());
                            dtoOnK.setId(storeProduct.getId());
                            dtoOnJ.getChildren().add(dtoOnK);
                        }
                    }
                    dtoOnI.getChildren().add(dtoOnJ);
                }
                list.add(dtoOnI);
            }
            return list;
        }
        return null;
    }

    @Override
    public List<ProductTreeNodeVo> getProductTreeByStorageId(Long storageId) {
        List<KxStoreCategory> storeCategoryList = storeCategoryMapper.selectList(new LambdaQueryWrapper<KxStoreCategory>().orderByAsc(KxStoreCategory::getPid));
        if (storeCategoryList != null && storeCategoryList.size() > 0) {
            Integer recordLevelOne = 0;
            List<KxStoreProduct> storeProductList = baseMapper.getProductTitleAllByStorageId(storageId);
            List<ProductTreeNodeVo> list = new ArrayList<>();
            for (KxStoreCategory categoryDO : storeCategoryList) {
                if (categoryDO.getPid() == 0L) {
                    recordLevelOne++;
                }
            }
            Integer recordLevelTwo = storeCategoryList.size();
            for (int i = 0; i < recordLevelOne; i++) {
                KxStoreCategory categoryOnI = storeCategoryList.get(i);    //一级类目
                ProductTreeNodeVo dtoOnI = new ProductTreeNodeVo();
                dtoOnI.setLabel(categoryOnI.getCateName());
                dtoOnI.setValue("C_" + categoryOnI.getId());
                dtoOnI.setId(categoryOnI.getId());
                dtoOnI.setChildren(new LinkedList<>());
                for (int j = recordLevelOne; j < recordLevelTwo; j++) {
                    KxStoreCategory categoryOnJ = storeCategoryList.get(j);    //二级类目
                    if (!categoryOnJ.getPid().equals(dtoOnI.getId())) {
                        continue;
                    }
                    ProductTreeNodeVo dtoOnJ = new ProductTreeNodeVo();
                    dtoOnJ.setLabel(categoryOnJ.getCateName());
                    dtoOnJ.setValue("C_" + categoryOnJ.getId());
                    dtoOnJ.setId(categoryOnJ.getId());
                    dtoOnJ.setChildren(new LinkedList<>());
                    for (int k = 0; k < storeProductList.size(); k++) {
                        if (k != 0 && storeProductList.get(k - 1).getCateId().equals(dtoOnJ.getId()) && !storeProductList.get(k).getCateId().equals(dtoOnJ.getId())) {
                            break;
                        }
                        KxStoreProduct storeProduct = storeProductList.get(k);        //商品
                        if (storeProduct.getCateId().equals(dtoOnJ.getId())) {
                            ProductTreeNodeVo dtoOnK = new ProductTreeNodeVo();
                            dtoOnK.setLabel(storeProduct.getStoreName());
                            dtoOnK.setValue("G_" + storeProduct.getId());
                            dtoOnK.setId(storeProduct.getId());
                            dtoOnJ.getChildren().add(dtoOnK);
                        }
                    }
                    dtoOnI.getChildren().add(dtoOnJ);
                }
                list.add(dtoOnI);
            }
            return list;
        }
        return null;
    }

    @Override
    public KxStoreProductVo detailGoodsByStorageId(Long storageId, Long productId) {

        //是否为活动商品
        KxStoreProductVo product = baseMapper.getProductByIdAndStorageId(productId, storageId);
        if (ObjectUtils.isEmpty(product)) {
            throw new ServiceException("商品对象不存在");
        }
        //查询库存信息
        KxStock kxStock = new KxStock();
        kxStock.setProductId(productId);
        kxStock.setStorageId(storageId);
        kxStock = kxStockMapper.selectOne(new QueryWrapper<>(kxStock));

        KxStockVo stockVo = new KxStockVo();
        if (!ObjectUtils.isEmpty(kxStock)) {
            BeanUtils.copyProperties(kxStock, stockVo);
        }
        product.setKxStockVo(stockVo);
        return product;
    }

    @Override
    public KxStoreProductVo queryByIdVo(Long productId) {
        return baseMapper.selectVoById(productId);
    }
    /**
     * 组合规则属性算法
     *
     * @param fromatDetailDTOList
     * @return DetailDto
     */
    private DetailVo attrFormat(List<FromatDetailVo> fromatDetailDTOList) {

        List<String> data = new ArrayList<>();
        List<Map<String, Map<String, String>>> res = new ArrayList<>();

        fromatDetailDTOList.stream()
                .map(FromatDetailVo::getDetail)
                .forEach(i -> {
                    if (i == null || i.isEmpty()) {
                        throw new ServiceException("请至少添加一个规格值哦");
                    }
                    String str = ArrayUtil.join(i.toArray(), ",");
                    if (str.contains("-")) {
                        throw new ServiceException("规格值里包含'-',请重新添加");
                    }
                });

        if (fromatDetailDTOList.size() > 1) {
            for (int i = 0; i < fromatDetailDTOList.size() - 1; i++) {
                if (i == 0) {
                    data = fromatDetailDTOList.get(i).getDetail();
                }
                List<String> tmp = new LinkedList<>();
                for (String v : data) {
                    for (String g : fromatDetailDTOList.get(i + 1).getDetail()) {
                        String rep2 = "";
                        if (i == 0) {
                            rep2 = fromatDetailDTOList.get(i).getValue() + "_" + v + "-"
                                    + fromatDetailDTOList.get(i + 1).getValue() + "_" + g;
                        } else {
                            rep2 = v + "-"
                                    + fromatDetailDTOList.get(i + 1).getValue() + "_" + g;
                        }

                        tmp.add(rep2);

                        if (i == fromatDetailDTOList.size() - 2) {
                            Map<String, Map<String, String>> rep4 = new LinkedHashMap<>();
                            Map<String, String> reptemp = new LinkedHashMap<>();
                            for (String h : Arrays.asList(rep2.split("-"))) {
                                List<String> rep3 = Arrays.asList(h.split("_"));
                                if (rep3.size() > 1) {
                                    reptemp.put(rep3.get(0), rep3.get(1));
                                } else {
                                    reptemp.put(rep3.get(0), "");
                                }
                            }
                            rep4.put("detail", reptemp);

                            res.add(rep4);
                        }
                    }

                }

                if (!tmp.isEmpty()) {
                    data = tmp;
                }
            }
        } else {
            List<String> dataArr = new ArrayList<>();
            for (FromatDetailVo fromatDetailVo : fromatDetailDTOList) {
                for (String str : fromatDetailVo.getDetail()) {
                    Map<String, Map<String, String>> map2 = new LinkedHashMap<>();
                    dataArr.add(fromatDetailVo.getValue() + "_" + str);
                    Map<String, String> map1 = new LinkedHashMap<>();
                    map1.put(fromatDetailVo.getValue(), str);
                    map2.put("detail", map1);
                    res.add(map2);
                }
            }
            String s = StrUtil.join("-", dataArr);
            data.add(s);
        }

        DetailVo detailDto = new DetailVo();
        detailDto.setData(data);
        detailDto.setRes(res);

        return detailDto;
    }

    /**
     * 增加表头
     *
     * @param headerMap     headerMap
     * @param headerMapList headerMapList
     * @param align         align
     */
    private void addMap(Map<String, Object> headerMap, List<Map<String, Object>> headerMapList, String align, boolean isActivity) {
        headerMap.put("title", "图片");
        headerMap.put("slot", "pic");
        headerMap.put("align", align);
        headerMap.put("minWidth", 80);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "售价");
        headerMap.put("slot", "price");
        headerMap.put("align", align);
        headerMap.put("minWidth", 120);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "成本价");
        headerMap.put("slot", "cost");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "原价");
        headerMap.put("slot", "otPrice");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "库存");
        headerMap.put("slot", "stock");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "产品编号");
        headerMap.put("slot", "barCode");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "重量(KG)");
        headerMap.put("slot", "weight");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "体积(m³)");
        headerMap.put("slot", "volume");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "所需兑换积分");
        headerMap.put("slot", "integral");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        if (isActivity) {
            headerMap.put("title", "拼团价");
            headerMap.put("slot", "pinkPrice");
            headerMap.put("align", align);
            headerMap.put("minWidth", 140);
            headerMapList.add(ObjectUtil.clone(headerMap));

            headerMap.put("title", "拼团活动库存");
            headerMap.put("slot", "pinkStock");
            headerMap.put("align", align);
            headerMap.put("minWidth", 140);
            headerMapList.add(ObjectUtil.clone(headerMap));

            headerMap.put("title", "秒杀价");
            headerMap.put("slot", "seckillPrice");
            headerMap.put("align", align);
            headerMap.put("minWidth", 140);
            headerMapList.add(ObjectUtil.clone(headerMap));

            headerMap.put("title", "秒杀活动库存");
            headerMap.put("slot", "seckillStock");
            headerMap.put("align", align);
            headerMap.put("minWidth", 140);
            headerMapList.add(ObjectUtil.clone(headerMap));
        }

        headerMap.put("title", "操作");
        headerMap.put("slot", "action");
        headerMap.put("align", align);
        headerMap.put("minWidth", 70);
        headerMapList.add(ObjectUtil.clone(headerMap));
    }


}
