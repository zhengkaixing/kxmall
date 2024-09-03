package com.kxmall.web.controller.user.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.user.domain.bo.KxUserBillBo;
import com.kxmall.user.domain.vo.KxUserBillVo;

/**
 * 用户账单Service接口
 *
 * @author kxmall
 * @date 2023-02-14
 */
public interface IKxAppUserBillService {



    /**
     * 查询用户账单列表
     */
    TableDataInfo<KxUserBillVo> queryPageList(KxUserBillBo bo, PageQuery pageQuery);


}
