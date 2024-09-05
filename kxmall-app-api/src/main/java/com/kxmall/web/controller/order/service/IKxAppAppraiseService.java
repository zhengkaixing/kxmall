package com.kxmall.web.controller.order.service;

import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.order.domain.bo.AppraiseRequestBo;
import com.kxmall.order.domain.vo.KxStoreAppraiseVo;

/**
 * @author 郅兴开源团队-小黑
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


    /**
     * 增加评价
     * @param bo
     */
    Boolean addAppraise(AppraiseRequestBo bo, Long userId);

}
