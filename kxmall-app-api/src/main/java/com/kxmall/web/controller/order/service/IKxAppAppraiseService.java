package com.kxmall.web.controller.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.order.domain.vo.KxStoreAppraiseVo;
import com.kxmall.storage.domain.vo.KxStockVo;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/6
 */
public interface IKxAppAppraiseService {

    /**
     * 获取评论
     * @param producId
     * @param pageNo
     * @param pageSize
     * @param state
     * @return
     */
    TableDataInfo<KxStoreAppraiseVo> getProductAppraiseByPage(Long producId, Integer pageNo, Integer pageSize, Integer state);
}
