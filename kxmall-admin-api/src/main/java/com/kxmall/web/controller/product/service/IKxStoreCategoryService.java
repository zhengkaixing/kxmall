package com.kxmall.web.controller.product.service;

import com.kxmall.product.domain.bo.KxStoreCategoryBo;
import com.kxmall.product.domain.vo.KxStoreCategoryVo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品分类Service接口
 *
 * @author kxmall
 * @date 2023-02-07
 */
public interface IKxStoreCategoryService {

    /**
     * 查询商品分类
     */
    KxStoreCategoryVo queryById(Long id);

    /**
     * 查询商品分类列表
     */
    TableDataInfo<KxStoreCategoryVo> queryPageList(KxStoreCategoryBo bo, PageQuery pageQuery);

    /**
     * 查询商品分类列表
     */
    List<KxStoreCategoryVo> queryList(KxStoreCategoryBo bo);

    /**
     * 新增商品分类
     */
    Boolean insertByBo(KxStoreCategoryBo bo);

    /**
     * 修改商品分类
     */
    Boolean updateByBo(KxStoreCategoryBo bo);

    /**
     * 校验并批量删除商品分类信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Long selectCountByPid(Long id);

    /**
     * 检测分类是否操过二级
     * @param pid 父级id
     * @return boolean
     */
    boolean checkCategory(Long pid);

    /**
     * 构建树形
     * @param storeCategoryVos 分类列表
     * @return map
     */
    Map<String, Object> buildTree(List<KxStoreCategoryVo> storeCategoryVos);

}
