package com.kxmall.print;

import com.kxmall.order.domain.vo.KxStoreOrderVo;

/**
 * Description: 管理员打印
 * User: admin
 * Date: 2019/12/27
 * Time: 16:15
 */
public interface AdminPrintBizService {

    public void newOrderPrint(KxStoreOrderVo kxStoreOrder);


}
