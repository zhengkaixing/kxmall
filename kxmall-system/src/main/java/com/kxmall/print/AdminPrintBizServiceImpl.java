package com.kxmall.print;

import com.kxmall.common.core.domain.PrintBean;
import com.kxmall.common.core.domain.PrintItemBean;
import com.kxmall.common.utils.FeieyunPrint;
import com.kxmall.order.domain.vo.KxStoreOrderProductVo;
import com.kxmall.order.domain.vo.KxStoreOrderVo;
import com.kxmall.storage.domain.KxStorage;
import com.kxmall.storage.mapper.KxStorageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 打印
 * User: admin
 * Date: 2019/12/27
 * Time: 16:22
 */
@Service
public class AdminPrintBizServiceImpl implements AdminPrintBizService {

    private static final Logger logger = LoggerFactory.getLogger(AdminPrintBizServiceImpl.class);

    @Resource
    private KxStorageMapper storageMapper;

    @Override
    public void newOrderPrint(KxStoreOrderVo kxStoreOrder) {
        KxStorage storageDO = storageMapper.selectById(kxStoreOrder.getStoreId());
        if (ObjectUtils.isEmpty(storageDO) || storageDO.getPrintSwitch() == 0) {
            return;
        }
        PrintBean printBean = PrintBean.builder()
                .orderNo(kxStoreOrder.getOrderId())
                .orderTime(kxStoreOrder.getCreateTime())
                .remark(kxStoreOrder.getRemark())
                .totalMoney(kxStoreOrder.getPayPrice())
                .address(kxStoreOrder.getUserAddress())
                .name(kxStoreOrder.getRealName())
                .phone(kxStoreOrder.getUserPhone())
                .predictTime(kxStoreOrder.getPredictTime())
                .orderType(kxStoreOrder.getShippingType())
                .storageName(storageDO.getName()).build();

        List<PrintItemBean> itemBeans = new ArrayList<>();
        for (KxStoreOrderProductVo productVo : kxStoreOrder.getProductList()) {
            PrintItemBean build = PrintItemBean.builder()
                    .num(productVo.getNum())
                    .price(productVo.getPrice())
                    .name(productVo.getProductTitle()).build();
            itemBeans.add(build);
        }
        printBean.setItemBeans(itemBeans);
        FeieyunPrint.print(storageDO.getPrintSn(), storageDO.getPrintUkey(), storageDO.getPrintAcount(), printBean);
    }
}
