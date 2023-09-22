package com.kxmall.web.controller.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.product.domain.KxStoreCategory;
import com.kxmall.product.domain.vo.KxStoreCategoryVo;
import com.kxmall.product.mapper.KxStoreCategoryMapper;
import com.kxmall.web.controller.product.service.IKxAppCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/6
 */

@RequiredArgsConstructor
@Service
public class KxAppCategoryService implements IKxAppCategoryService {

    private final KxStoreCategoryMapper baseMapper;

    public static final String CA_CATEGORY_ID_HASH = "CA_CATEGORY_ID_HASH";

    public static final String CA_CATEGORY_LIST = "CA_CATEGORY_LIST";

    @Override
    public List<Long> getCategoryFamily(Long cateId) {
        Map<String, String> hashAll = RedisUtils.getCacheMap(CA_CATEGORY_ID_HASH);
        if (hashAll == null || ObjectUtils.isEmpty(hashAll)) {
            //构建此Hash表
            final Map<String,String> newHash = new HashMap<>();
            //将所有子节点查询出来
            List<KxStoreCategoryVo> categoryList = categoryList();
            categoryList.forEach(topItem -> {
                if (!CollectionUtils.isEmpty(topItem.getChildren()))
                    topItem.getChildren().forEach(subItem -> {
                        if (!CollectionUtils.isEmpty(subItem.getChildren()))
                            subItem.getChildren().forEach(leafItem -> {
                                newHash.put("S" + leafItem.getId(), subItem.getId() + "_" + topItem.getId());
                            });
                    });
            });
            hashAll = newHash;
            RedisUtils.setCacheMap(CA_CATEGORY_ID_HASH, hashAll);
        }

        LinkedList<Long> ids = new LinkedList<>();
        ids.add(cateId);
        String str = hashAll.get("S" + cateId);
        if (!StringUtils.isEmpty(str)) {
            String[] split = str.split("_");
            ids.add(new Long(split[0]));
            ids.add(new Long(split[1]));
        }
        return ids;
    }

    @Override
    public List<KxStoreCategory> selectList(LambdaQueryWrapper<KxStoreCategory> eq) {
        return baseMapper.selectList(eq);
    }

    @Override
    public KxStoreCategory selectById(Long categoryId) {
        return baseMapper.selectById(categoryId);
    }


    @Override
    public List<KxStoreCategoryVo> categoryList() {
        List<KxStoreCategoryVo> categoryDTOListFormCache = RedisUtils.getCacheList(CA_CATEGORY_LIST);
        if (CollectionUtils.isEmpty(categoryDTOListFormCache)) {
            //return categoryDTOListFormCache;
        }
        //从数据库查询
        List<KxStoreCategory> categoryDOList = baseMapper.selectList(new LambdaQueryWrapper<>());
        //组装DTO
        List<KxStoreCategoryVo> storeCategoryVoLinkedList = new LinkedList<>();
        categoryDOList.forEach(categoryDO -> {
            if (categoryDO.getPid() == 0) {
                KxStoreCategoryVo categoryDTO = new KxStoreCategoryVo();
                BeanUtils.copyProperties(categoryDO, categoryDTO);
                storeCategoryVoLinkedList.add(categoryDTO);
            }
        });

        //遍历二、三级
        storeCategoryVoLinkedList.forEach(storeCategoryVo -> {
            categoryDOList.forEach(categoryDO -> {
                if (categoryDO.getPid().equals(storeCategoryVo.getId())) {
                    List<KxStoreCategoryVo> childrenList = storeCategoryVo.getChildren();
                    if (childrenList == null) {
                        childrenList = new LinkedList<>();
                        storeCategoryVo.setChildren(childrenList);
                    }
                    KxStoreCategoryVo childKxStoreCategoryVo = new KxStoreCategoryVo();
                    BeanUtils.copyProperties(categoryDO, childKxStoreCategoryVo);
                    childKxStoreCategoryVo.setChildren(new LinkedList<>());
                    childrenList.add(childKxStoreCategoryVo);
                    categoryDOList.forEach(leaf -> {
                        if (childKxStoreCategoryVo.getId().equals(leaf.getPid())) {
                            KxStoreCategoryVo leafKxStoreCategoryVo = new KxStoreCategoryVo();
                            BeanUtils.copyProperties(leaf, leafKxStoreCategoryVo);
                            childKxStoreCategoryVo.getChildren().add(leafKxStoreCategoryVo);
                        }
                    });
                }
            });
        });

        //放入缓存
        RedisUtils.setCacheList(CA_CATEGORY_LIST, storeCategoryVoLinkedList);
        return storeCategoryVoLinkedList;
    }
}
