package com.kxmall.web.controller.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.user.domain.KxUserLevelSetting;
import com.kxmall.user.domain.KxUserTask;
import com.kxmall.user.domain.bo.KxUserTaskBo;
import com.kxmall.user.domain.vo.KxUserTaskVo;
import com.kxmall.user.mapper.KxUserTaskMapper;
import com.kxmall.web.controller.user.service.IKxUserLevelSettingService;
import com.kxmall.web.controller.user.service.IKxUserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@RequiredArgsConstructor
@Service
public class KxUserTaskServiceImpl implements IKxUserTaskService {

    private final KxUserTaskMapper baseMapper;
    private final IKxUserLevelSettingService userLevelSettingService;

    @Override
    public TableDataInfo<KxUserTaskVo> queryPageList(KxUserTaskBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxUserTask> lqw = buildQueryWrapper(bo);

        IPage<KxUserTaskVo> userTaskVoIPage = baseMapper.selectVoPage(pageQuery.build(), lqw);
        for (KxUserTaskVo record : userTaskVoIPage.getRecords()) {
            KxUserLevelSetting level = userLevelSettingService.queryByLevel(record.getLevelId());
            if (!ObjectUtils.isEmpty(level)) {
                record.setLevalName(level.getName());
            }
        }
        return TableDataInfo.build(userTaskVoIPage);
    }

    @Override
    public List<KxUserTaskVo> queryList(KxUserTaskBo bo) {
        LambdaQueryWrapper<KxUserTask> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxUserTask> buildQueryWrapper(KxUserTaskBo bo) {
        LambdaQueryWrapper<KxUserTask> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), KxUserTask::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getRealName()), KxUserTask::getRealName, bo.getRealName());
        lqw.like(StringUtils.isNotBlank(bo.getTaskType()), KxUserTask::getTaskType, bo.getTaskType());
        lqw.eq(bo.getNumber() != null, KxUserTask::getNumber, bo.getNumber());
        lqw.eq(bo.getLevelId() != null, KxUserTask::getLevelId, bo.getLevelId());
        lqw.eq(bo.getSort() != null, KxUserTask::getSort, bo.getSort());
        lqw.eq(bo.getIsShow() != null, KxUserTask::getIsShow, bo.getIsShow());
        lqw.eq(bo.getIsMust() != null, KxUserTask::getIsMust, bo.getIsMust());
        lqw.like(StringUtils.isNotBlank(bo.getIllustrate()), KxUserTask::getIllustrate, bo.getIllustrate());
        return lqw;
    }

    @Override
    public KxUserTaskVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public Boolean insertByBo(KxUserTaskBo bo) {
        KxUserTask add = BeanUtil.toBean(bo, KxUserTask.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(KxUserTaskBo bo) {
        KxUserTask update = BeanUtil.toBean(bo, KxUserTask.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void validEntityBeforeSave(KxUserTask entity) {
        // 预留校验
    }

    @Override
    public Boolean deleteWithValidByIds(List<Long> ids, Boolean isValid) {
        if (isValid) {
            // 预留校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
