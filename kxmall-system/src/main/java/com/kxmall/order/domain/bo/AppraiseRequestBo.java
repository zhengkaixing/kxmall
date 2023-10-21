package com.kxmall.order.domain.bo;

import lombok.Data;

import java.util.List;

/*
前端评价时，传入信息的数据结构
@author admin
@date  2019/7/6 - 14:26
*/
@Data
public class AppraiseRequestBo {

    private Long orderId;


    /**
     * 仓库id
     */
    private Long storageId;

    private List<KxStoreAppraiseBo> appraiseDTOList;

}


