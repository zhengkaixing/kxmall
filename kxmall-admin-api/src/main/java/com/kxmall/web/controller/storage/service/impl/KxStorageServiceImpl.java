package com.kxmall.web.controller.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.StorageBusinessStatusType;
import com.kxmall.common.enums.StorageStatusType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.storage.domain.KxStorage;
import com.kxmall.storage.domain.bo.KxStorageBo;
import com.kxmall.storage.domain.vo.KxStorageVo;
import com.kxmall.storage.mapper.KxStorageMapper;
import com.kxmall.web.controller.storage.service.IKxStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 仓库管理Service业务层处理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@RequiredArgsConstructor
@Service
public class KxStorageServiceImpl implements IKxStorageService {

    private static final String STORAGE_INFO_PREFIX = "STORAGE_INFO_";

    private final KxStorageMapper baseMapper;

    /**
     * 查询仓库管理
     */
    @Override
    public KxStorageVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询仓库管理列表
     */
    @Override
    public TableDataInfo<KxStorageVo> queryPageList(KxStorageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxStorage> lqw = buildQueryWrapper(bo);
        Page<KxStorageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询仓库管理列表
     */
    @Override
    public List<KxStorageVo> queryList(KxStorageBo bo) {
        LambdaQueryWrapper<KxStorage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxStorage> buildQueryWrapper(KxStorageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxStorage> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), KxStorage::getName, bo.getName());
        lqw.eq(bo.getProvince() != null, KxStorage::getProvince, bo.getProvince());
        lqw.eq(bo.getCity() != null, KxStorage::getCity, bo.getCity());
        lqw.eq(bo.getCounty() != null, KxStorage::getCounty, bo.getCounty());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), KxStorage::getAddress, bo.getAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getAdcode()), KxStorage::getAdcode, bo.getAdcode());
        lqw.eq(bo.getState() != null, KxStorage::getState, bo.getState());
        lqw.eq(bo.getLongitude() != null, KxStorage::getLongitude, bo.getLongitude());
        lqw.eq(bo.getLatitude() != null, KxStorage::getLatitude, bo.getLatitude());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), KxStorage::getPhone, bo.getPhone());
        lqw.like(StringUtils.isNotBlank(bo.getLeaderName()), KxStorage::getLeaderName, bo.getLeaderName());
        lqw.eq(bo.getOperatingState() != null, KxStorage::getOperatingState, bo.getOperatingState());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessStartTime()), KxStorage::getBusinessStartTime, bo.getBusinessStartTime());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliveryStartTime()), KxStorage::getDeliveryStartTime, bo.getDeliveryStartTime());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessStopTime()), KxStorage::getBusinessStopTime, bo.getBusinessStopTime());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliveryStopTime()), KxStorage::getDeliveryStopTime, bo.getDeliveryStopTime());
        lqw.eq(bo.getDeliveryRadius() != null, KxStorage::getDeliveryRadius, bo.getDeliveryRadius());
        lqw.eq(bo.getAutomatic() != null, KxStorage::getAutomatic, bo.getAutomatic());
        lqw.eq(bo.getPrintSwitch() != null, KxStorage::getPrintSwitch, bo.getPrintSwitch());
        lqw.eq(StringUtils.isNotBlank(bo.getPrintAcount()), KxStorage::getPrintAcount, bo.getPrintAcount());
        lqw.eq(StringUtils.isNotBlank(bo.getPrintUkey()), KxStorage::getPrintUkey, bo.getPrintUkey());
        lqw.eq(StringUtils.isNotBlank(bo.getPrintSn()), KxStorage::getPrintSn, bo.getPrintSn());
        return lqw;
    }

    /**
     * 新增仓库管理
     */
    @Override
    public Boolean insertByBo(KxStorageBo bo) {

        KxStorage add = BeanUtil.toBean(bo, KxStorage.class);
        add.setState(StorageStatusType.NOMRAL.getCode());
        add.setOperatingState(StorageBusinessStatusType.REST.getCode());
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            RedisUtils.deleteKeys(STORAGE_INFO_PREFIX + "*");
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改仓库管理
     */
    @Override
    public Boolean updateByBo(KxStorageBo bo) {
        KxStorage update = BeanUtil.toBean(bo, KxStorage.class);
        validEntityBeforeSave(update);
        boolean b = baseMapper.updateById(update) > 0;
        if (b) {
            RedisUtils.deleteKeys(STORAGE_INFO_PREFIX + "*");
        }
        return b;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxStorage entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除仓库管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean updateStateToNomral(KxStorageBo bo) {
        if (ObjectUtils.isEmpty(bo.getId())) {
            throw new ServiceException("前置仓资料不存在");
        }
        if (baseMapper.batchUpdateState(Collections.singletonList(bo.getId()), StorageStatusType.NOMRAL.getCode()) <= 0) {
            throw new ServiceException("前置仓资料不存在");
        } else {
            RedisUtils.deleteKeys(STORAGE_INFO_PREFIX + "*");
        }
        return true;
    }

    @Override
    public Boolean updateStateToAbort(KxStorageBo bo) {
        if (ObjectUtils.isEmpty(bo.getId())) {
            throw new ServiceException("前置仓资料不存在");
        }
        if (baseMapper.batchUpdateState(Collections.singletonList(bo.getId()), StorageStatusType.ABORT.getCode()) <= 0) {
            throw new ServiceException("前置仓资料不存在");
        } else {
            RedisUtils.deleteKeys(STORAGE_INFO_PREFIX + "*");
        }
        return true;
    }

    @Override
    public Boolean updateBusinessStateToOpen(KxStorageBo bo) {
        if (ObjectUtils.isEmpty(bo.getId())) {
            throw new ServiceException("前置仓资料不存在");
        }
        if (baseMapper.batchUpdateOperatingState(Collections.singletonList(bo.getId()), StorageBusinessStatusType.BUSINESS.getCode()) <= 0) {
            throw new ServiceException("前置仓资料不存在");
        } else {
            RedisUtils.deleteKeys(STORAGE_INFO_PREFIX + "*");
        }
        return true;
    }

    @Override
    public Boolean updateBusinessStateToRest(KxStorageBo bo) {
        if (ObjectUtils.isEmpty(bo.getId())) {
            throw new ServiceException("前置仓资料不存在");
        }
        if (baseMapper.batchUpdateOperatingState(Collections.singletonList(bo.getId()), StorageBusinessStatusType.REST.getCode()) <= 0) {
            throw new ServiceException("前置仓资料不存在");
        } else {
            RedisUtils.deleteKeys(STORAGE_INFO_PREFIX + "*");
        }
        return true;
    }

    @Override
    public List<KxStorageVo> options() {
        return null;
    }

    @Override
    public String getStorageQrcodeImage() {
        return null;
    }

    @Override
    public Boolean printTest(KxStorageBo bo) {
        return null;
    }


}
