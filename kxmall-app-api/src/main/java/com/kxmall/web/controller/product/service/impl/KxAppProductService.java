package com.kxmall.web.controller.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.ProductStatusType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.order.domain.KxStoreAppraise;
import com.kxmall.order.domain.vo.KxStoreAppraiseVo;
import com.kxmall.order.mapper.KxStoreAppraiseMapper;
import com.kxmall.product.domain.KxStoreCategory;
import com.kxmall.product.domain.KxStoreProduct;
import com.kxmall.product.domain.vo.KxStoreProductVo;
import com.kxmall.product.mapper.KxStoreCategoryMapper;
import com.kxmall.product.mapper.KxStoreProductMapper;
import com.kxmall.storage.domain.KxStock;
import com.kxmall.storage.domain.vo.KxStockVo;
import com.kxmall.storage.mapper.KxStockMapper;
import com.kxmall.web.controller.order.service.IKxAppAppraiseService;
import com.kxmall.web.controller.product.service.IKxAppCategoryService;
import com.kxmall.web.controller.product.service.IKxAppProductService;
import com.kxmall.web.controller.user.service.IKxUserFootprintService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/3
 */
@RequiredArgsConstructor
@Service
public class KxAppProductService implements IKxAppProductService {

    private final KxStoreProductMapper baseMapper;

    private final IKxAppCategoryService categoryService;

    private final IKxAppAppraiseService appraiseService;

    private final IKxUserFootprintService userFootprintService;

    private final KxStockMapper stockMapper;


    /**
     * PRODUCT 分页缓存
     */
    public static final String CA_PRODUCT_PAGE_PREFIX = "CA_PRODUCT_PAGE_";

    /**
     * 缓存  CA_PRODUCT_+productId
     */
    public static final String CA_PRODUCT_PREFIX = "CA_PRODUCT_";

    @Override
    public TableDataInfo<KxStoreProductVo> getGoodsPage(Integer pageNo, Integer pageSize, Long categoryId, String orderBy, Boolean isAsc, String title) {

        LambdaQueryWrapper<KxStoreProduct> wrapper = new LambdaQueryWrapper<>();

        if (!StringUtils.isEmpty(title)) {
            wrapper.like(KxStoreProduct::getStoreName, title);
        } else {
            //若关键字为空，尝试从缓存取列表
            TableDataInfo<KxStoreProductVo> objFromCache = RedisUtils.getCacheObject(CA_PRODUCT_PAGE_PREFIX + categoryId + "_" + pageNo + "_" + pageSize + "_" + orderBy + "_" + isAsc);
            if (objFromCache != null) {
                return objFromCache;
            }
        }

        if (orderBy != null && isAsc != null) {
            wrapper.last("ORDER BY " + orderBy + (isAsc ? " ASC" : " DESC"));
        }

        if (categoryId != null && categoryId != 0L) {

            List<KxStoreCategory> childrenList = categoryService.selectList(new LambdaQueryWrapper<KxStoreCategory>().eq(KxStoreCategory::getPid, categoryId));

            if (CollectionUtils.isEmpty(childrenList)) {
                //目标节点为叶子节点,即三级类目
                wrapper.eq(KxStoreProduct::getCateId, categoryId);
            } else {
                //目标节点存在子节点
                LinkedList<Long> childrenIds = new LinkedList<>();
                KxStoreCategory category = categoryService.selectById(categoryId);

                // 检验传入类目是一级还是二级类目
                if (category.getPid() != 0L) {
                    //二级分类
                    childrenList.forEach(item -> {
                        childrenIds.add(item.getId());
                    });
                } else {
                    //一级分类
                    childrenList.forEach(item -> {
                        List<KxStoreCategory> leafList = categoryService.selectList(new LambdaQueryWrapper<KxStoreCategory>().eq(KxStoreCategory::getPid, item.getId()));
                        if (!CollectionUtils.isEmpty(leafList)) {
                            leafList.forEach(leafItem -> {
                                childrenIds.add(leafItem.getId());
                            });
                        }
                    });
                }
                wrapper.in(KxStoreProduct::getCateId, childrenIds);
            }
        }

        wrapper.eq(KxStoreProduct::getIsShow, ProductStatusType.SELLING.getCode());

        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(pageNo - 1);
        pageQuery.setPageSize(pageSize);

        Page<KxStoreProductVo> productPage = baseMapper.selectVoPage(pageQuery.build(), wrapper);

        TableDataInfo<KxStoreProductVo> tableDataInfo = TableDataInfo.build(productPage);

        if (StringUtils.isEmpty(title)) {
            //若关键字为空，制作缓存
            RedisUtils.setCacheObject(CA_PRODUCT_PAGE_PREFIX + categoryId + "_" + pageNo + "_" + pageSize + "_" + orderBy + "_" + isAsc, tableDataInfo);
        }
        return tableDataInfo;
    }

    @Override
    public TableDataInfo<KxStoreProductVo> getGoodsPageByStorage(Long storageId, Integer pageNo, Integer pageSize, Long categoryId, String orderBy, Boolean isAsc, String title) {
        //缓存key
        String keyCache = CA_PRODUCT_PAGE_PREFIX + storageId + "_" + categoryId + "_" + pageNo + "_" + pageSize + "_" + orderBy + "_" + isAsc;
        //开始组装条件search
        if (StringUtils.isEmpty(title)) {
            //若关键字为空，尝试从缓存取列表
            TableDataInfo<KxStoreProductVo> objFromCache = RedisUtils.getCacheObject(keyCache);
            if (objFromCache != null) {
                //return objFromCache;
            }
        }
        if (orderBy != null && isAsc != null) {
            orderBy = "b."+orderBy;
        }
        LinkedList<Long> childrenIds = new LinkedList<>();
        if (categoryId != null && categoryId != 0L) {

            List<KxStoreCategory> childrenList = categoryService.selectList(new LambdaQueryWrapper<KxStoreCategory>().eq(KxStoreCategory::getPid, categoryId));
            if (!CollectionUtils.isEmpty(childrenList)) {
                //一级分类
                childrenList.forEach(item -> childrenIds.add(item.getId()));
                //使用in查询，就不需要等于查询
                categoryId = null;
            }
        }

        Integer offset = (pageNo - 1) * pageSize;
        Integer size = pageSize;

        List<KxStoreProductVo> productPage = baseMapper.selectPageByStorage(offset,size, title, categoryId, childrenIds, storageId, orderBy, isAsc);

        Long count = baseMapper.selectPageByStorageCount(title, categoryId, childrenIds, storageId, orderBy, isAsc);

        TableDataInfo<KxStoreProductVo> tableDataInfo = new TableDataInfo<>(productPage,count);

        if (StringUtils.isEmpty(title)) {
            //若关键字为空，制作缓存
            RedisUtils.setCacheObject(CA_PRODUCT_PAGE_PREFIX + categoryId + "_" + pageNo + "_" + pageSize + "_" + orderBy + "_" + isAsc, tableDataInfo);
        }
        return tableDataInfo;
    }

    @Override
    public KxStoreProductVo getGoodsByStorage(Long storageId,Long productId, Long userId) {
        //获取缓存
        KxStoreProductVo storeProductVo = RedisUtils.getCacheObject(CA_PRODUCT_PREFIX + storageId + "_" + productId);
        if (storeProductVo != null) {
            //获取第一页评论
            List<KxStoreAppraiseVo> storeAppraisePage = appraiseService.getProductAppraiseByPage(productId, 1, 10, 1).getRows();
            storeProductVo.setAppraisePage(storeAppraisePage);
            //新增该用户查看印记
            if (userId != null) {
                userFootprintService.addOrUpdateFootprint(userId, productId);
            }
            return storeProductVo;
        }
        //是否为活动商品
        KxStoreProduct product  = baseMapper.selectById(productId);
        if (ObjectUtils.isEmpty(product)) {
            throw new ServiceException("商品对象不存在");
        }
        KxStoreProductVo kxStoreProductVo = new KxStoreProductVo();
        BeanUtils.copyProperties(product, kxStoreProductVo);


        //查询库存信息
        KxStock kxStock = new KxStock();
        kxStock.setProductId(productId);
        kxStock.setStorageId(storageId);
        kxStock = stockMapper.selectOne(new QueryWrapper<>(kxStock));

        KxStockVo stockVo = new KxStockVo();
        if (!ObjectUtils.isEmpty(kxStock)) {
            BeanUtils.copyProperties(kxStock, stockVo);
        }
        kxStoreProductVo.setKxStockVo(stockVo);
        //类目族
        kxStoreProductVo.setCateIdList(categoryService.getCategoryFamily(product.getCateId()));

        //放入缓存
        RedisUtils.setCacheObject(CA_PRODUCT_PREFIX + storageId + "_" + productId, kxStoreProductVo);
        //获取第一页评论
        List<KxStoreAppraiseVo> storeAppraisePage = appraiseService.getProductAppraiseByPage(productId, 1, 10, 1).getRows();
        kxStoreProductVo.setAppraisePage(storeAppraisePage);
        if (userId != null) {
            userFootprintService.addOrUpdateFootprint(userId, productId);
        }
        return kxStoreProductVo;
    }
}
