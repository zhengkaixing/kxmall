package com.kxmall.web.controller.user.service;

import com.kxmall.user.domain.vo.KxUserBillVo;
import com.kxmall.user.domain.bo.KxUserBillBo;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户账单Service接口
 *
 * @author kxmall
 * @date 2023-02-14
 */
public interface IKxUserBillService {

    /**
     * 查询用户账单
     */
    KxUserBillVo queryById(Long id);

    /**
     * 查询用户账单列表
     */
    TableDataInfo<KxUserBillVo> queryPageList(KxUserBillBo bo, PageQuery pageQuery);

    /**
     * 查询用户账单列表
     */
    List<KxUserBillVo> queryList(KxUserBillBo bo);

    /**
     * 新增用户账单
     */
    Boolean insertByBo(KxUserBillBo bo);

    /**
     * 修改用户账单
     */
    Boolean updateByBo(KxUserBillBo bo);

    /**
     * 校验并批量删除用户账单信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 增加支出流水
     * @param uid uid
     * @param title 账单标题
     * @param category 明细种类
     * @param type 明细类型
     * @param number 明细数字
     * @param balance 剩余
     * @param mark 备注
     */
    void expend(Long uid,String title,String category,String type,double number,double balance,String mark);

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
    void income(Long uid,String title,String category,String type,double number,
                double balance,String mark,String linkid);
}
