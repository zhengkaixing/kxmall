package com.kxmall.group.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kxmall.common.enums.StatusType;
import com.kxmall.group.domain.KxGroupShopProduct;
import com.kxmall.group.domain.vo.KxGroupShopVo;
import com.kxmall.group.mapper.KxGroupShopMapper;
import com.kxmall.group.mapper.KxGroupShopProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Description:
 * User: admin
 * Date: 2019/11/25
 * Time: 11:30
 */
@Service
public class GroupShopBizService {

    @Autowired
    private KxGroupShopMapper groupShopMapper;

    @Autowired
    private KxGroupShopProductMapper groupShopProductMapper;

    public KxGroupShopVo getGroupShopById(Long id, Long storageId) {
        KxGroupShopVo detail = groupShopMapper.detail(id,storageId);
        if (detail == null || detail.getStatus() == StatusType.LOCK.getCode()) {
            return null;
        }
        List<KxGroupShopProduct> groupShopSkuList = groupShopProductMapper.selectList(new QueryWrapper<KxGroupShopProduct>().eq("group_shop_id", id));
        detail.setGroupShopSkuList(groupShopSkuList);
        return detail;
    }

}
