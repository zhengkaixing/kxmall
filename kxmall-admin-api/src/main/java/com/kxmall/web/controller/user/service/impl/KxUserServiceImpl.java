package com.kxmall.web.controller.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.web.controller.user.service.IKxUserBillService;
import com.kxmall.web.controller.user.service.IKxUserService;
import com.kxmall.user.domain.KxUser;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.BillDetailEnum;
import com.kxmall.common.enums.ShopCommonEnum;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.user.domain.bo.KxUserBo;
import com.kxmall.user.domain.vo.KxPromUserVo;
import com.kxmall.user.domain.vo.KxUserVo;
import com.kxmall.user.mapper.KxUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户Service业务层处理
 *
 * @author kxmall
 * @date 2023-02-14
 */
@RequiredArgsConstructor
@Service
public class KxUserServiceImpl implements IKxUserService {

    private final KxUserMapper baseMapper;

    private final IKxUserBillService userBillService;

    /**
     * 查询用户
     */
    @Override
    public KxUserVo queryById(Long uid) {
        return baseMapper.selectVoById(uid);
    }

    /**
     * 查询用户列表
     */
    @Override
    public TableDataInfo<KxUserVo> queryPageList(KxUserBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxUser> lqw = buildQueryWrapper(bo);
        Page<KxUserVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户列表
     */
    @Override
    public List<KxUserVo> queryList(KxUserBo bo) {
        LambdaQueryWrapper<KxUser> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxUser> buildQueryWrapper(KxUserBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxUser> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getUsername()), KxUser::getUsername, bo.getUsername());
        lqw.eq(StringUtils.isNotBlank(bo.getPassword()), KxUser::getPassword, bo.getPassword());
        lqw.like(StringUtils.isNotBlank(bo.getRealName()), KxUser::getRealName, bo.getRealName());
        lqw.eq(bo.getBirthday() != null, KxUser::getBirthday, bo.getBirthday());
        lqw.eq(StringUtils.isNotBlank(bo.getCardId()), KxUser::getCardId, bo.getCardId());
        lqw.eq(StringUtils.isNotBlank(bo.getMark()), KxUser::getMark, bo.getMark());
        lqw.eq(bo.getPartnerId() != null, KxUser::getPartnerId, bo.getPartnerId());
        lqw.eq(bo.getGroupId() != null, KxUser::getGroupId, bo.getGroupId());
        lqw.like(StringUtils.isNotBlank(bo.getNickname()), KxUser::getNickname, bo.getNickname());
        lqw.eq(StringUtils.isNotBlank(bo.getAvatar()), KxUser::getAvatar, bo.getAvatar());
        lqw.likeRight(StringUtils.isNotBlank(bo.getPhone()), KxUser::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getAddIp()), KxUser::getAddIp, bo.getAddIp());
        lqw.eq(StringUtils.isNotBlank(bo.getLastIp()), KxUser::getLastIp, bo.getLastIp());
        lqw.eq(bo.getNowMoney() != null, KxUser::getNowMoney, bo.getNowMoney());
        lqw.eq(bo.getBrokeragePrice() != null, KxUser::getBrokeragePrice, bo.getBrokeragePrice());
        lqw.eq(bo.getIntegral() != null, KxUser::getIntegral, bo.getIntegral());
        lqw.eq(bo.getSignNum() != null, KxUser::getSignNum, bo.getSignNum());
        lqw.eq(bo.getStatus() != null, KxUser::getStatus, bo.getStatus());
        lqw.eq(bo.getLevel() != null, KxUser::getLevel, bo.getLevel());
        lqw.eq(bo.getSpreadUid() != null, KxUser::getSpreadUid, bo.getSpreadUid());
        lqw.eq(bo.getSpreadTime() != null, KxUser::getSpreadTime, bo.getSpreadTime());
        lqw.eq(StringUtils.isNotBlank(bo.getUserType()), KxUser::getUserType, bo.getUserType());
        lqw.eq(bo.getIsPromoter() != null, KxUser::getIsPromoter, bo.getIsPromoter());
        lqw.eq(bo.getPayCount() != null, KxUser::getPayCount, bo.getPayCount());
        lqw.eq(bo.getSpreadCount() != null, KxUser::getSpreadCount, bo.getSpreadCount());
        lqw.eq(StringUtils.isNotBlank(bo.getAddres()), KxUser::getAddres, bo.getAddres());
        lqw.eq(bo.getAdminid() != null, KxUser::getAdminid, bo.getAdminid());
        lqw.eq(ObjectUtils.isNotEmpty(bo.getLoginType()), KxUser::getLoginType, bo.getLoginType());
        lqw.eq(StringUtils.isNotBlank(bo.getWxProfile()), KxUser::getWxProfile, bo.getWxProfile());
        lqw.eq(bo.getIsDel() != null, KxUser::getIsDel, bo.getIsDel());
        return lqw;
    }

    /**
     * 新增用户
     */
    @Override
    public Boolean insertByBo(KxUserBo bo) {
        KxUser add = BeanUtil.toBean(bo, KxUser.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setUid(add.getUid());
        }
        return flag;
    }

    /**
     * 修改用户
     */
    @Override
    public Boolean updateByBo(KxUserBo bo) {
        KxUser update = BeanUtil.toBean(bo, KxUser.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxUser entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 查看下级
     *
     * @return
     */
    @Override
    public List<KxPromUserVo> querySpread(KxUserBo bo) {
        return this.getUserSpreadGrade(bo.getUid(), 1, 999, bo.getGrade(), "", "");
    }


    /**
     * 获取我的分销下人员列表
     *
     * @param uid     uid
     * @param page    page
     * @param limit   limit
     * @param grade   ShopCommonEnum.GRADE_0
     * @param keyword 关键字搜索
     * @param sort    排序
     * @return list
     */
    @Override
    public List<KxPromUserVo> getUserSpreadGrade(Long uid, int page, int limit, Integer grade,
                                                 String keyword, String sort) {
        List<KxUser> userList = baseMapper.selectList(new LambdaQueryWrapper<KxUser>()
            .eq(KxUser::getSpreadUid, uid));
        List<Long> userIds = userList.stream()
            .map(KxUser::getUid)
            .collect(Collectors.toList());

        List<KxPromUserVo> list = new ArrayList<>();
        if (userIds.isEmpty()) {
            return list;
        }

        if (StrUtil.isBlank(sort)) {
            sort = "u.uid desc";
        }

        Page<KxUser> pageModel = new Page<>(page, limit);
        if (ShopCommonEnum.GRADE_0.getValue().equals(grade)) {//-级
            list = baseMapper.getUserSpreadCountList(pageModel, userIds,
                keyword, sort);
        } else {//二级
            List<KxUser> userListT = baseMapper.selectList(new LambdaQueryWrapper<KxUser>()
                .in(KxUser::getSpreadUid, userIds));
            List<Long> userIdsT = userListT.stream()
                .map(KxUser::getUid)
                .collect(Collectors.toList());
            if (userIdsT.isEmpty()) {
                return list;
            }
            list = baseMapper.getUserSpreadCountList(pageModel, userIdsT,
                keyword, sort);

        }
        return list;
    }

    @Override
    public void onStatus(Long id, Integer status) {
        if (ShopCommonEnum.IS_STATUS_1.getValue().equals(status)) {
            status = ShopCommonEnum.IS_STATUS_1.getValue();
        } else {
            status = ShopCommonEnum.IS_STATUS_0.getValue();
        }
        KxUser user = new KxUser();
        user.setStatus(status);
        baseMapper.update(user, new LambdaQueryWrapper<KxUser>().eq(KxUser::getUid, id));
    }

    @Override
    public void updateMoney(KxUserBo param) {
        KxUser kxUser = baseMapper.selectById(param.getUid());
        double newMoney = 0d;
        String mark = "";
        if (param.getPtype() == 1) {
            mark = "系统增加了" + param.getMoney() + "余额";
            newMoney = NumberUtil.add(kxUser.getNowMoney(), param.getMoney()).doubleValue();
            userBillService.income(kxUser.getUid(), "系统增加余额", BillDetailEnum.CATEGORY_1.getValue(),
                BillDetailEnum.TYPE_6.getValue(), param.getMoney(), newMoney, mark, "");
        } else {
            mark = "系统扣除了" + param.getMoney() + "余额";
            newMoney = NumberUtil.sub(kxUser.getNowMoney(), param.getMoney()).doubleValue();
            if (newMoney < 0) {
                newMoney = 0d;
            }
            userBillService.expend(kxUser.getUid(), "系统减少余额",
                BillDetailEnum.CATEGORY_1.getValue(),
                BillDetailEnum.TYPE_7.getValue(),
                param.getMoney(), newMoney, mark);
        }
        kxUser.setNowMoney(BigDecimal.valueOf(newMoney));
        baseMapper.insertOrUpdate(kxUser);
    }

    @Override
    public KxUserVo selectByUid(Long uid) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<KxUser>().eq(KxUser::getUid, uid));
    }

}
