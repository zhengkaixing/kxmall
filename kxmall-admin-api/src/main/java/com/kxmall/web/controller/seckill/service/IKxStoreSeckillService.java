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
public interface IKxStoreSeckillService {

    /**
     * 查询商品秒杀
     */
    KxStoreSeckillVo queryById(Long id);

    /**
     * 查询商品秒杀列表
     */
    TableDataInfo<KxStoreSeckillVo> queryPageList(KxStoreSeckillBo bo, PageQuery pageQuery);

    /**
     * 查询商品秒杀列表
     */
    List<KxStoreSeckillVo> queryList(KxStoreSeckillBo bo);

    /**
     * 新增商品秒杀
     */
    Boolean insertByBo(KxStoreSeckillBo bo);

    /**
     * 修改商品秒杀
     */
    Boolean updateByBo(KxStoreSeckillBo bo);

    /**
     * 校验并批量删除商品秒杀信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
