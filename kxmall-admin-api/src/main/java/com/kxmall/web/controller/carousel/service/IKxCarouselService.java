package com.kxmall.web.controller.carousel.service;

import com.kxmall.carousel.domain.vo.KxCarouselVo;
import com.kxmall.carousel.domain.bo.KxCarouselBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商铺广告Service接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface IKxCarouselService {

    /**
     * 查询商铺广告
     */
    KxCarouselVo queryById(Long id);

    /**
     * 查询商铺广告列表
     */
    TableDataInfo<KxCarouselVo> queryPageList(KxCarouselBo bo, PageQuery pageQuery);

    /**
     * 查询商铺广告列表
     */
    List<KxCarouselVo> queryList(KxCarouselBo bo);

    /**
     * 新增商铺广告
     */
    Boolean insertByBo(KxCarouselBo bo);

    /**
     * 修改商铺广告
     */
    Boolean updateByBo(KxCarouselBo bo);

    /**
     * 校验并批量删除商铺广告信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
