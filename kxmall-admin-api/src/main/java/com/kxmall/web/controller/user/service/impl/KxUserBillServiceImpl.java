package com.kxmall.web.controller.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.enums.BillEnum;
import com.kxmall.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kxmall.user.domain.bo.KxUserBillBo;
import com.kxmall.user.domain.vo.KxUserBillVo;
import com.kxmall.user.domain.KxUserBill;
import com.kxmall.user.mapper.KxUserBillMapper;
import com.kxmall.web.controller.user.service.IKxUserBillService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 用户账单Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-14
 */
@RequiredArgsConstructor
@Service
public class KxUserBillServiceImpl implements IKxUserBillService {

    private final KxUserBillMapper baseMapper;

    /**
     * 查询用户账单
     */
    @Override
    public KxUserBillVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户账单列表
     */
    @Override
    public TableDataInfo<KxUserBillVo> queryPageList(KxUserBillBo bo, PageQuery pageQuery) {
        QueryWrapper<KxUserBill> lqw = buildQueryWrapperQuery(bo);

        Page<KxUserBillVo> result = baseMapper.selectVoPageList(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户账单列表
     */
    @Override
    public List<KxUserBillVo> queryList(KxUserBillBo bo) {
        LambdaQueryWrapper<KxUserBill> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxUserBill> buildQueryWrapper(KxUserBillBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxUserBill> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUid() != null, KxUserBill::getUid, bo.getUid());
        lqw.eq(StringUtils.isNotBlank(bo.getLinkId()), KxUserBill::getLinkId, bo.getLinkId());
        lqw.eq(bo.getPm() != null, KxUserBill::getPm, bo.getPm());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), KxUserBill::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), KxUserBill::getCategory, bo.getCategory());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), KxUserBill::getType, bo.getType());
        lqw.eq(bo.getNumber() != null, KxUserBill::getNumber, bo.getNumber());
        lqw.eq(bo.getBalance() != null, KxUserBill::getBalance, bo.getBalance());
        lqw.eq(StringUtils.isNotBlank(bo.getMark()), KxUserBill::getMark, bo.getMark());
        lqw.eq(bo.getStatus() != null, KxUserBill::getStatus, bo.getStatus());
        lqw.eq(bo.getIsDel() != null, KxUserBill::getIsDel, bo.getIsDel());
        return lqw;
    }


    private QueryWrapper<KxUserBill> buildQueryWrapperQuery(KxUserBillBo bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<KxUserBill> lqw = Wrappers.query();
        lqw.eq(bo.getUid() != null, "b.uid", bo.getUid());
        lqw.eq(StringUtils.isNotBlank(bo.getLinkId()), "b.link_id", bo.getLinkId());
        lqw.eq(bo.getPm() != null, "b.pm", bo.getPm());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), "b.title", bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), "b.category", bo.getCategory());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), "b.type", bo.getType());
        lqw.eq(bo.getNumber() != null, "b.number", bo.getNumber());
        lqw.eq(bo.getBalance() != null, "b.balance", bo.getBalance());
        lqw.eq(StringUtils.isNotBlank(bo.getMark()), "b.mark", bo.getMark());
        lqw.eq(bo.getStatus() != null, "b.status", bo.getStatus());
        lqw.eq(bo.getIsDel() != null, "b.is_del", bo.getIsDel());
        lqw.likeRight(bo.getIsDel() != null, "u.nickname", bo.getNickname());
        return lqw;
    }

    /**
     * 新增用户账单
     */
    @Override
    public Boolean insertByBo(KxUserBillBo bo) {
        KxUserBill add = BeanUtil.toBean(bo, KxUserBill.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户账单
     */
    @Override
    public Boolean updateByBo(KxUserBillBo bo) {
        KxUserBill update = BeanUtil.toBean(bo, KxUserBill.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxUserBill entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户账单
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

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
    @Override
    public void expend(Long uid,String title,String category,String type,double number,double balance,String mark){
        KxUserBill userBill = KxUserBill.builder()
            .uid(uid)
            .title(title)
            .category(category)
            .type(type)
            .number(BigDecimal.valueOf(number))
            .balance(BigDecimal.valueOf(balance))
            .mark(mark)
            .pm(BillEnum.PM_0.getValue())
            .build();

        baseMapper.insert(userBill);
    }


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
    @Override
    public void income(Long uid,String title,String category,String type,double number,
                       double balance,String mark,String linkid){
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
