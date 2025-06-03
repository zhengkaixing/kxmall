package com.kxmall.order.biz;

import com.kxmall.common.enums.BillEnum;
import com.kxmall.user.domain.KxUserBill;
import com.kxmall.user.mapper.KxUserBillMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author kaixin
 * @version 1.0
 * @date 2024/9/1
 */
@Service
public class BillBizService {

    @Resource
    private KxUserBillMapper baseMapper;

    /**
     * 增加收入/支入流水
     * @param uid uid
     * @param title 账单标题
     * @param category 明细种类
     * @param type 明细类型
     * @param number 明细数字
     * @param balance 剩余
     * @param mark 备注
     * @param linkid 关联id
     */
    public void income(Long uid,String title,String category,String type,double number,
                       double balance,String mark,String linkid){
        //积分增加0，不予记录
        if(number==0D){
            return;
        }
        KxUserBill userBill = KxUserBill.builder()
                .uid(uid)
                .title(title)
                .category(category)
                .type(type)
                .number(BigDecimal.valueOf(number))
                .balance(BigDecimal.valueOf(balance))
                .mark(mark)
                .pm(BillEnum.PM_1.getValue())
                .linkId(linkid)
                .build();

        baseMapper.insert(userBill);
    }

}
