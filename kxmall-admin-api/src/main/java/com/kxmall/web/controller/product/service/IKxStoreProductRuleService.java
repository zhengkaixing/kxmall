package com.kxmall.web.controller.product.service;

import com.kxmall.product.domain.bo.KxStoreProductRuleBo;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.product.domain.vo.KxStoreProductRuleVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品规格Service接口
 *
 * @author kxmall
 * @date 2023-02-08
 */
public interface IKxStoreProductRuleService {

    /**
     * 查询商品规格
     */
    KxStoreProductRuleVo queryById(Long id);

    /**
     * 查询商品规格列表
     */
    TableDataInfo<KxStoreProductRuleVo> queryPageList(KxStoreProductRuleBo bo, PageQuery pageQuery);

    /**
     * 查询商品规格列表
     */
    List<KxStoreProductRuleVo> queryList(KxStoreProductRuleBo bo);

    /**
     * 新增商品规格
     */
    Boolean insertByBo(KxStoreProductRuleBo bo);

    /**
     * 修改商品规格
     */
    Boolean updateByBo(KxStoreProductRuleBo bo);

    /**
     * 校验并批量删除商品规格信息
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);

    List<KxStoreProductRuleVo> queryListAll();
}
