package com.kxmall.web.controller.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.user.domain.KxUserBill;
import com.kxmall.user.domain.bo.KxUserBillBo;
import com.kxmall.user.domain.vo.KxUserBillVo;
import com.kxmall.user.mapper.KxUserBillMapper;
import com.kxmall.web.controller.user.service.IKxAppUserBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户账单Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-14
 */
@RequiredArgsConstructor
@Service
public class KxAppUserBillServiceImpl implements IKxAppUserBillService {

    private final KxUserBillMapper baseMapper;


    /**
     * 查询用户账单列表
     */
    @Override
    public TableDataInfo<KxUserBillVo> queryPageList(KxUserBillBo bo, PageQuery pageQuery) {
        QueryWrapper<KxUserBill> lqw = buildQueryWrapperQuery(bo);

        Page<KxUserBillVo> result = baseMapper.selectVoPageList(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
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
        lqw.orderByDesc("b.create_time");
        return lqw;
    }



    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxUserBill entity){
        //TODO 做一些数据校验,如唯一约束
    }





}
