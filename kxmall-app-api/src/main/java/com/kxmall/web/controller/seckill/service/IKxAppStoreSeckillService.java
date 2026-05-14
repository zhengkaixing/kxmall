package com.kxmall.web.controller.seckill.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.seckill.domain.bo.KxStoreSeckillBo;
import com.kxmall.seckill.domain.vo.KxStoreSeckillVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品秒杀Service接口
 *
 * @author kxmall
 * @date 2024-05-15
 */
public interface IKxAppStoreSeckillService {

    KxStoreSeckillVo queryById(Long id);

    TableDataInfo<KxStoreSeckillVo> queryPageList(KxStoreSeckillBo bo, PageQuery pageQuery);

    List<KxStoreSeckillVo> queryList(KxStoreSeckillBo bo);

    Boolean insertByBo(KxStoreSeckillBo bo);

    Boolean updateByBo(KxStoreSeckillBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<KxStoreSeckillVo> listCurrent(Long storageId, Integer sort);
}
