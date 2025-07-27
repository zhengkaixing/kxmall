package com.kxmall.web.controller.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.domain.entity.SysDictData;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.BillDetailEnum;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.order.biz.BillBizService;
import com.kxmall.system.service.ISysDictTypeService;
import com.kxmall.user.domain.KxUser;
import com.kxmall.user.domain.KxUserSign;
import com.kxmall.user.domain.bo.KxUserSignBo;
import com.kxmall.user.domain.vo.KxUserSignVo;
import com.kxmall.user.domain.vo.KxUserVo;
import com.kxmall.user.mapper.KxUserMapper;
import com.kxmall.user.mapper.KxUserSignMapper;
import com.kxmall.web.controller.user.service.IKxAppUserSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 签到记录Service业务层处理
 *
 * @author kxmall
 * @date 2024-08-26
 */
@RequiredArgsConstructor
@Service
public class KxAppUserSignServiceImpl implements IKxAppUserSignService {

    private final KxUserSignMapper baseMapper;

    private final ISysDictTypeService dictTypeService;

    private final KxUserMapper userMapper;

    private final BillBizService billService;

    /**
     * 查询签到记录
     */
    @Override
    public KxUserSignVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询签到记录列表
     */
    @Override
    public TableDataInfo<KxUserSignVo> queryPageList(KxUserSignBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxUserSign> lqw = buildQueryWrapper(bo);
        Page<KxUserSignVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询签到记录列表
     */
    @Override
    public List<KxUserSignVo> queryList(KxUserSignBo bo) {
        LambdaQueryWrapper<KxUserSign> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxUserSign> buildQueryWrapper(KxUserSignBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxUserSign> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUid() != null, KxUserSign::getUid, bo.getUid());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), KxUserSign::getTitle, bo.getTitle());
        lqw.eq(bo.getNumber() != null, KxUserSign::getNumber, bo.getNumber());
        lqw.eq(bo.getBalance() != null, KxUserSign::getBalance, bo.getBalance());
        lqw.eq(bo.getIsDel() != null, KxUserSign::getIsDel, bo.getIsDel());
        lqw.orderByDesc(KxUserSign::getCreateTime);
        return lqw;
    }

    /**
     * 新增签到记录
     */
    @Override
    public Boolean insertByBo(KxUserSignBo bo) {
        KxUserSign add = BeanUtil.toBean(bo, KxUserSign.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改签到记录
     */
    @Override
    public Boolean updateByBo(KxUserSignBo bo) {
        KxUserSign update = BeanUtil.toBean(bo, KxUserSign.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxUserSign entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除签到记录
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public KxUserVo getSignInUser(Long userId) {

        KxUserVo userVo = userMapper.selectVoById(userId);

        Long sumSignDay = baseMapper.selectCount(new LambdaQueryWrapper<KxUserSign>().eq(KxUserSign::getUid,userId));
        boolean isDaySign = this.getToDayIsSign(userId);
        boolean isYesterDaySign = this.getYesterDayIsSign(userId);
        userVo.setSumSignDay(sumSignDay);
        userVo.setIsDaySign(isDaySign);
        userVo.setIsYesterDaySign(isYesterDaySign);
        if(!isDaySign && !isYesterDaySign) {
            userVo.setSignNum(0L);
        }

        return userVo;
    }

    private boolean getYesterDayIsSign(Long userId) {
        Date today = DateUtil.beginOfDay(new Date());
        Date yesterday = DateUtil.beginOfDay(DateUtil.yesterday());

        Long count = baseMapper.selectCount(new LambdaQueryWrapper<KxUserSign>().eq(KxUserSign::getUid,userId)
                .lt(KxUserSign::getCreateTime,today)
                .ge(KxUserSign::getCreateTime,yesterday));
        return count > 0;
    }

    private boolean getToDayIsSign(Long userId) {
        Date today = DateUtil.beginOfDay(new Date());
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<KxUserSign>().eq(KxUserSign::getUid, userId).ge(KxUserSign::getCreateTime, today));
        return count > 0;
    }

    @Override
    public Long sign(Long userId) {

        KxUserVo userVo = userMapper.selectVoById(userId);

        List<SysDictData> sginRule = dictTypeService.selectDictDataByType("sgin_rule");
        if(ObjectUtil.isNull(sginRule) || sginRule.isEmpty()) {
            throw new ServiceException("请先配置签到天数");
        }

        boolean isDaySign = this.getToDayIsSign(userId);
        if(isDaySign) {
            throw new ServiceException("已签到");
        }
        //积分
        long signNumber = 0L;
        //签到次数
        Long userSignNum = userVo.getSignNum();
        if(getYesterDayIsSign(userVo.getUid())){
            if(userVo.getSignNum() > (sginRule.size() - 1)){
                userSignNum = 0L;
            }
        }else{
            userSignNum = 0L;
        }
        int index = 0;
        for (SysDictData map : sginRule) {
            if(index == userSignNum){
                signNumber = Long.parseLong(map.getDictValue());
                break;
            }
            index++;
        }

        userSignNum += 1;

        KxUserSign userSign = new KxUserSign();
        userSign.setUid(userVo.getUid());
        String title = "签到奖励";
        if(userSignNum == sginRule.size()){
            title = "连续签到奖励";
        }
        userSign.setTitle(title);
        userSign.setNumber(signNumber);
        userSign.setBalance(userVo.getIntegral().longValue());
        baseMapper.insert(userSign);

        //用户积分增加
        KxUser user = new KxUser();
        user.setIntegral(NumberUtil.add(userVo.getIntegral(), signNumber));
        user.setUid(userVo.getUid());
        user.setSignNum(userSignNum);

        int res = userMapper.updateById(user);
        if(res==0) {
            throw new ServiceException("签到失败");
        }

        //插入流水
        billService.income(userVo.getUid(),title, BillDetailEnum.CATEGORY_2.getValue(),
                BillDetailEnum.TYPE_10.getValue(),signNumber,user.getIntegral().doubleValue(),
                "","");

        return signNumber;
    }
}
