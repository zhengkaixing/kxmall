package com.kxmall.web.controller.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.StorageBusinessStatusType;
import com.kxmall.common.enums.StorageStatusType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.FeieyunPrint;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.common.utils.file.FileUtils;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.storage.domain.KxStorage;
import com.kxmall.storage.domain.bo.KxStorageBo;
import com.kxmall.storage.domain.vo.KxStorageVo;
import com.kxmall.storage.mapper.KxStorageMapper;
import com.kxmall.system.service.ISysConfigService;
import com.kxmall.web.controller.storage.service.IKxStorageService;
import com.kxmall.wechat.WxMpConfiguration;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

import static com.kxmall.common.constant.ProductCacheConstants.STORAGE_INFO_PREFIX;

/**
 * 仓库管理Service业务层处理
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-08-27
 */
@RequiredArgsConstructor
@Service
public class KxStorageServiceImpl implements IKxStorageService {


    private final KxStorageMapper baseMapper;

    private final ISysConfigService configService;

    /**
     * 查询仓库管理
     */
    @Override
    public KxStorageVo queryById(Long id) {
        KxStorageVo kxStorageVo = baseMapper.selectVoById(id);
        if (CollectionUtils.isEmpty(kxStorageVo.getPaths())) {
            kxStorageVo.setPaths(new ArrayList<>());
        }
        return kxStorageVo;
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
        lqw.in(CollectionUtils.isNotEmpty(bo.getStorageIds()), KxStorage::getId, bo.getStorageIds());
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
    public String getStorageQrcodeImage(Long storageId) {
        try {
            if (org.springframework.util.ObjectUtils.isEmpty(storageId)) {
                throw new ServiceException("管理员系统未知异常");
            }
            Map<String, Object> sendObject = new HashMap<>(1);
            sendObject.put("storageId", storageId);
            //判断一下h5是否配置
            String appid = configService.selectConfigByKeyNoCache("h5_appid");
            if (StringUtils.isEmpty(appid)) {
                return "";
            }
            WxMpQrCodeTicket mpQrCodeTicket = WxMpConfiguration.getWxMpService().getQrcodeService().qrCodeCreateTmpTicket(JSONObject.toJSONString(sendObject), 7200);
            File file = WxMpConfiguration.getWxMpService().getQrcodeService().qrCodePicture(mpQrCodeTicket);
            return FileUtils.fileToBase64(file);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(),500);
        }
    }

    @Override
    public String printTest(KxStorageBo bo) {
        if (StringUtils.isEmpty(bo.getPrintSn())) {
            throw new ServiceException("sn不能为空",500);
        }
        if (StringUtils.isEmpty(bo.getPrintUkey())) {
            throw new ServiceException("Ukey不能为空",500);
        }
        if (StringUtils.isEmpty(bo.getPrintAcount())) {
            throw new ServiceException("Acount不能为空",500);
        }
        return FeieyunPrint.printTset(bo.getPrintSn(),bo.getPrintUkey(),bo.getPrintAcount());
    }


}
