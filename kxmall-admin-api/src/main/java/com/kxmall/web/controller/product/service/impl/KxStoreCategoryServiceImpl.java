package com.kxmall.web.controller.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kxmall.product.domain.KxStoreCategory;
import com.kxmall.product.domain.bo.KxStoreCategoryBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kxmall.product.domain.vo.KxStoreCategoryVo;
import com.kxmall.product.mapper.KxStoreCategoryMapper;
import com.kxmall.web.controller.product.service.IKxStoreCategoryService;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品分类Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-07
 */
@RequiredArgsConstructor
@Service
public class KxStoreCategoryServiceImpl implements IKxStoreCategoryService {

    private final KxStoreCategoryMapper baseMapper;

    /**
     * 查询商品分类
     */
    @Override
    public KxStoreCategoryVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品分类列表
     */
    @Override
    public TableDataInfo<KxStoreCategoryVo> queryPageList(KxStoreCategoryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStoreCategory> lqw = buildQueryWrapper(bo);
        Page<KxStoreCategoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品分类列表
     */
    @Override
    public List<KxStoreCategoryVo> queryList(KxStoreCategoryBo bo) {
        LambdaQueryWrapper<KxStoreCategory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStoreCategory> buildQueryWrapper(KxStoreCategoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxStoreCategory> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPid() != null, KxStoreCategory::getPid, bo.getPid());
        lqw.like(StringUtils.isNotBlank(bo.getCateName()), KxStoreCategory::getCateName, bo.getCateName());
        lqw.eq(bo.getSort() != null, KxStoreCategory::getSort, bo.getSort());
        lqw.eq(StringUtils.isNotBlank(bo.getPic()), KxStoreCategory::getPic, bo.getPic());
        lqw.eq(bo.getIsShow() != null, KxStoreCategory::getIsShow, bo.getIsShow());
        return lqw;
    }

    /**
     * 新增商品分类
     */
    @Override
    public Boolean insertByBo(KxStoreCategoryBo bo) {
        KxStoreCategory add = BeanUtil.toBean(bo, KxStoreCategory.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品分类
     */
    @Override
    public Boolean updateByBo(KxStoreCategoryBo bo) {
        KxStoreCategory update = BeanUtil.toBean(bo, KxStoreCategory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStoreCategory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品分类
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Long selectCountByPid(Long id) {
        return baseMapper.selectCount(new LambdaQueryWrapper<KxStoreCategory>()
            .eq(KxStoreCategory::getPid, id));
    }

    @Override
    public boolean checkCategory(Long pid) {
        if(pid == 0) {
            return true;
        }
        KxStoreCategory kxStoreCategory = baseMapper.selectOne(new LambdaQueryWrapper<KxStoreCategory>()
            .eq(KxStoreCategory::getId,pid));
        return kxStoreCategory.getPid() <= 0;
    }

    @Override
    public Map<String, Object> buildTree(List<KxStoreCategoryVo> storeCategoryVos) {
        Set<KxStoreCategoryVo> trees = new LinkedHashSet<>();
        Set<KxStoreCategoryVo> cates = new LinkedHashSet<>();
        List<String> deptNames = storeCategoryVos.stream().map(KxStoreCategoryVo::getCateName)
            .collect(Collectors.toList());

        boolean isChild;
        List<KxStoreCategory> categories = baseMapper.selectList();
        for (KxStoreCategoryVo deptDTO : storeCategoryVos) {
            deptDTO.setLabel(deptDTO.getCateName());
            isChild = false;
            if ("0".equals(deptDTO.getPid().toString())) {
                trees.add(deptDTO);
            }
            for (KxStoreCategoryVo it : storeCategoryVos) {
                if (it.getPid().equals(deptDTO.getId())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<KxStoreCategoryVo>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if (isChild) {
                cates.add(deptDTO);
            }
            for (KxStoreCategory category : categories) {
                if (category.getId().equals(deptDTO.getPid()) && !deptNames.contains(category.getCateName())) {
                    cates.add(deptDTO);
                }
            }
        }


        if (CollectionUtils.isEmpty(trees)) {
            trees = cates;
        }
        Integer totalElements = storeCategoryVos.size();

        Map<String, Object> map = new HashMap<>(2);
        map.put("totalElements", totalElements);
        map.put("content", CollectionUtils.isEmpty(trees) ? storeCategoryVos : trees);
        return map;
    }
}
