package com.kxmall.web.controller.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.ShopCommonEnum;
import com.kxmall.common.utils.OrderUtil;
import com.kxmall.user.domain.KxUser;
import com.kxmall.user.domain.KxUserLevelSetting;
import com.kxmall.user.domain.KxUserMemberLevel;
import com.kxmall.user.domain.vo.KxUserLevelSettingVo;
import com.kxmall.user.domain.vo.KxUserMemberLevelVo;
import com.kxmall.user.domain.vo.KxUserVo;
import com.kxmall.user.domain.vo.TaskDto;
import com.kxmall.user.domain.vo.UserLevelDto;
import com.kxmall.user.mapper.KxUserLevelSettingMapper;
import com.kxmall.user.mapper.KxUserMemberLevelMapper;
import com.kxmall.user.mapper.KxUserMapper;
import com.kxmall.web.controller.user.service.IKxAppUserLevelService;
import com.kxmall.web.controller.user.service.IKxAppUserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
@Service
public class KxAppUserLevelServiceImpl implements IKxAppUserLevelService {

    private final KxUserMemberLevelMapper memberLevelMapper;
    private final KxUserMapper userMapper;
    private final IKxAppUserTaskService iKxUserTaskService;
    private final KxUserLevelSettingMapper userLevelSettingMapper;

    public KxUserMemberLevel getUserLevel(Long uid, Long grade) {
        LambdaQueryWrapper<KxUserMemberLevel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KxUserMemberLevel::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                .eq(KxUserMemberLevel::getUid, uid)
                .orderByDesc(KxUserMemberLevel::getGrade);
        if (grade != null) {
            wrapper.lt(KxUserMemberLevel::getGrade, grade);
        }
        KxUserMemberLevel userLevel = memberLevelMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(userLevel)) {
            return null;
        }
        if (ShopCommonEnum.IS_FOREVER_1.getValue().equals(userLevel.getIsForever())) {
            return userLevel;
        }
        long nowTime = OrderUtil.getSecondTimestampTwo();
        if (nowTime > userLevel.getValidTime()) {
            if (ShopCommonEnum.IS_STATUS_1.getValue().equals(userLevel.getStatus())) {
                userLevel.setStatus(ShopCommonEnum.IS_STATUS_0.getValue());
                memberLevelMapper.updateById(userLevel);
            }
            return this.getUserLevel(uid, userLevel.getGrade());
        }
        return userLevel;
    }

    @Override
    public UserLevelDto getLevelInfo(Long userId) {
        Long levelId = 0L;
        KxUserMemberLevel userLevel = getUserLevel(userId, null);

        if (userLevel != null) {
            levelId = userLevel.getLevelId();
        }

        List<KxUserLevelSettingVo> list = this.getLevelListAndGrade(levelId);
        if (list.isEmpty()) {
            throw new RuntimeException("请后台设置会员等级");
        }

        KxUserVo userVo = userMapper.selectVoById(userId);

        TaskDto taskDto = iKxUserTaskService.getTaskList(list.get(0).getId(), userId);

        UserLevelDto userLevelDto = new UserLevelDto();
        userLevelDto.setList(list);
        userLevelDto.setTask(taskDto);
        userLevelDto.setGrade(userVo.getLevel() == null ? 0L : userVo.getLevel().longValue());

        return userLevelDto;
    }

    @Override
    public boolean setLevelComplete(Long userId) {
        Long levelId = 0L;
        KxUserMemberLevel userLevel = this.getUserLevel(userId, null);
        if (userLevel != null) {
            levelId = userLevel.getLevelId();
        }

        Long nextLevelId = this.getNextLevelId(levelId);
        if (nextLevelId == 0L) {
            return false;
        }

        Long finishCount = iKxUserTaskService.getTaskComplete(nextLevelId, userId);

        Long totalTaskCount = iKxUserTaskService.getTaskCountByLevel(nextLevelId);

        if (finishCount != null && finishCount.equals(totalTaskCount)) {
            this.setUserLevel(userId, nextLevelId);
            return true;
        }
        return false;
    }

    private void setUserLevel(Long userId, Long levelId) {
        KxUserLevelSetting levelSetting = userLevelSettingMapper.selectById(levelId);
        if (ObjectUtil.isNull(levelSetting)) {
            return;
        }

        long validTime = levelSetting.getValidDate() * 86400;

        KxUserMemberLevel userLevel = new KxUserMemberLevel();
        userLevel.setIsForever(levelSetting.getIsForever());
        userLevel.setStatus(ShopCommonEnum.IS_STATUS_1.getValue());
        userLevel.setGrade(levelSetting.getGrade());
        userLevel.setUid(userId);
        userLevel.setLevelId(levelId);
        userLevel.setDiscount(levelSetting.getDiscount().intValue());

        if (ShopCommonEnum.IS_FOREVER_1.getValue().equals(levelSetting.getIsForever())) {
            userLevel.setValidTime(0L);
        } else {
            userLevel.setValidTime(validTime + OrderUtil.getSecondTimestampTwo());
        }

        userLevel.setMark("恭喜你成为了" + levelSetting.getName());
        memberLevelMapper.insert(userLevel);

        KxUser kxUser = new KxUser();
        kxUser.setLevel(levelId.intValue());
        kxUser.setUid(userId);
        userMapper.updateById(kxUser);
    }

    @Override
    public Long getNextLevelId(Long levelId) {
        List<KxUserLevelSetting> list = userLevelSettingMapper.selectList(
                new LambdaQueryWrapper<KxUserLevelSetting>()
                        .eq(KxUserLevelSetting::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                        .orderByAsc(KxUserLevelSetting::getGrade));

        Long grade = 0L;
        for (KxUserLevelSetting row : list) {
            if (row.getId() != null && row.getId().equals(levelId)) {
                grade = row.getGrade();
            }
        }

        KxUserLevelSetting next = userLevelSettingMapper.selectOne(
                new LambdaQueryWrapper<KxUserLevelSetting>()
                        .eq(KxUserLevelSetting::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                        .orderByAsc(KxUserLevelSetting::getGrade)
                        .gt(KxUserLevelSetting::getGrade, grade)
                        .last("limit 1"));

        if (ObjectUtil.isNull(next)) {
            return 0L;
        }
        return next.getId();
    }

    private List<KxUserLevelSettingVo> getLevelListAndGrade(Long levelId) {
        Long grade = 0L;
        List<KxUserLevelSetting> list = userLevelSettingMapper.selectList(
                new LambdaQueryWrapper<KxUserLevelSetting>()
                        .eq(KxUserLevelSetting::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                        .orderByAsc(KxUserLevelSetting::getGrade));

        List<KxUserLevelSettingVo> newList = BeanUtil.copyToList(list, KxUserLevelSettingVo.class);
        for (KxUserLevelSettingVo userLevelVo : newList) {
            if (userLevelVo.getId() != null && userLevelVo.getId().equals(levelId)) {
                grade = userLevelVo.getGrade();
            }
            if (grade.compareTo(userLevelVo.getGrade()) < 0) {
                userLevelVo.setIsClear(true);
            } else {
                userLevelVo.setIsClear(false);
            }
        }
        return newList;
    }

    @Override
    public KxUserMemberLevelVo queryById(Long id) {
        return memberLevelMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<KxUserMemberLevelVo> queryPageList(KxUserMemberLevelVo vo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxUserMemberLevel> lqw = buildQueryWrapper(vo);
        Page<KxUserMemberLevelVo> result = memberLevelMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<KxUserMemberLevelVo> queryList(KxUserMemberLevelVo vo) {
        LambdaQueryWrapper<KxUserMemberLevel> lqw = buildQueryWrapper(vo);
        return memberLevelMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxUserMemberLevel> buildQueryWrapper(KxUserMemberLevelVo vo) {
        LambdaQueryWrapper<KxUserMemberLevel> lqw = Wrappers.lambdaQuery();
        lqw.eq(vo.getMerId() != null, KxUserMemberLevel::getMerId, vo.getMerId());
        lqw.orderByAsc(KxUserMemberLevel::getGrade);
        return lqw;
    }

    @Override
    public Boolean insertByBo(KxUserMemberLevelVo vo) {
        KxUserMemberLevel add = BeanUtil.toBean(vo, KxUserMemberLevel.class);
        validEntityBeforeSave(add);
        boolean flag = memberLevelMapper.insert(add) > 0;
        if (flag) {
            vo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(KxUserMemberLevelVo vo) {
        KxUserMemberLevel update = BeanUtil.toBean(vo, KxUserMemberLevel.class);
        validEntityBeforeSave(update);
        return memberLevelMapper.updateById(update) > 0;
    }

    private void validEntityBeforeSave(KxUserMemberLevel entity) {
        // 预留校验
    }

    @Override
    public Boolean deleteWithValidByIds(List<Long> ids, Boolean isValid) {
        if (isValid) {
            // 预留校验
        }
        return memberLevelMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public BigDecimal setLevelPrice(BigDecimal price, Long uid) {
        LambdaQueryWrapper<KxUserMemberLevel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KxUserMemberLevel::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                .eq(KxUserMemberLevel::getUid, uid)
                .orderByDesc(KxUserMemberLevel::getGrade)
                .last("limit 1");
        KxUserMemberLevel userLevel = memberLevelMapper.selectOne(wrapper);
        KxUserLevelSetting setting = new KxUserLevelSetting();
        if (ObjectUtil.isNotNull(userLevel)) {
            setting = userLevelSettingMapper.selectById(userLevel.getLevelId());
        }
        if (ObjectUtil.isNotNull(userLevel) && ObjectUtil.isNotNull(setting)) {
            int discount = setting.getDiscount().intValue();
            return NumberUtil.mul(NumberUtil.div(discount, 100), price).setScale(2, RoundingMode.HALF_UP);
        }
        return null;
    }
}
