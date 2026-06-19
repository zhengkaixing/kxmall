package com.kxmall.web.controller.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.user.domain.KxUserSign;
import com.kxmall.user.domain.bo.KxUserSignBo;
import com.kxmall.user.domain.vo.KxUserSignVo;
import com.kxmall.user.mapper.KxUserSignMapper;
import com.kxmall.web.controller.user.service.IKxUserSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 签到记录Service业务层处理
 *
 * @author kxmall
 * @date 2024-08-26
 */
@RequiredArgsConstructor
@Service
public class KxUserSignServiceImpl implements IKxUserSignService {

    private final KxUserSignMapper baseMapper;

    @Override
    public KxUserSignVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<KxUserSignVo> queryPageList(KxUserSignBo bo, PageQuery pageQuery) {
        QueryWrapper<KxUserSign> lqw = buildQueryWrapperQuery(bo);
        Page<KxUserSignVo> result = baseMapper.selectVoPageList(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    private QueryWrapper<KxUserSign> buildQueryWrapperQuery(KxUserSignBo bo) {
        QueryWrapper<KxUserSign> lqw = Wrappers.query();
        lqw.eq(bo.getUid() != null, "b.uid", bo.getUid());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), "b.title", bo.getTitle());
        lqw.eq(bo.getNumber() != null, "b.number", bo.getNumber());
        lqw.eq(bo.getBalance() != null, "b.balance", bo.getBalance());
        lqw.eq(bo.getIsDel() != null, "b.is_del", bo.getIsDel());
        lqw.likeRight(StringUtils.isNotBlank(bo.getNickname()), "u.nickname", bo.getNickname());
        lqw.orderByDesc("b.create_time");
        return lqw;
    }

    @Override
    public List<KxUserSignVo> queryList(KxUserSignBo bo) {
        LambdaQueryWrapper<KxUserSign> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxUserSign> buildQueryWrapper(KxUserSignBo bo) {
        LambdaQueryWrapper<KxUserSign> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUid() != null, KxUserSign::getUid, bo.getUid());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), KxUserSign::getTitle, bo.getTitle());
        lqw.eq(bo.getNumber() != null, KxUserSign::getNumber, bo.getNumber());
        lqw.eq(bo.getBalance() != null, KxUserSign::getBalance, bo.getBalance());
        lqw.eq(bo.getIsDel() != null, KxUserSign::getIsDel, bo.getIsDel());
        return lqw;
    }

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

    @Override
    public Boolean updateByBo(KxUserSignBo bo) {
        KxUserSign update = BeanUtil.toBean(bo, KxUserSign.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void validEntityBeforeSave(KxUserSign entity) {
        // 预留校验
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 预留校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
