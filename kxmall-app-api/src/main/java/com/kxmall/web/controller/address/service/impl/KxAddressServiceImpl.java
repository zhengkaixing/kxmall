package com.kxmall.web.controller.address.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.address.domain.KxAddress;
import com.kxmall.address.domain.bo.KxAddressBo;
import com.kxmall.address.domain.vo.KxAddressVo;
import com.kxmall.address.mapper.KxAddressMapper;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.DefaultAddressStatusType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.web.controller.address.service.IKxAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户下单地址Service业务层处理
 *
 * @author kxmall
 * @date 2023-04-06
 */
@RequiredArgsConstructor
@Service
public class KxAddressServiceImpl implements IKxAddressService {

    private final KxAddressMapper baseMapper;

    /**
     * 查询用户下单地址
     */
    @Override
    public KxAddressVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户下单地址列表
     */
    @Override
    public TableDataInfo<KxAddressVo> queryPageList(KxAddressBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxAddress> lqw = buildQueryWrapper(bo);
        Page<KxAddressVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户下单地址列表
     */
    @Override
    public List<KxAddressVo> queryList(KxAddressBo bo) {
        LambdaQueryWrapper<KxAddress> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxAddress> buildQueryWrapper(KxAddressBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxAddress> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getProvince()), KxAddress::getProvince, bo.getProvince());
        lqw.eq(StringUtils.isNotBlank(bo.getCity()), KxAddress::getCity, bo.getCity());
        lqw.eq(StringUtils.isNotBlank(bo.getCounty()), KxAddress::getCounty, bo.getCounty());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), KxAddress::getAddress, bo.getAddress());
        lqw.eq(bo.getDefaultAddress() != null, KxAddress::getDefaultAddress, bo.getDefaultAddress());
        lqw.eq(bo.getUserId() != null, KxAddress::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), KxAddress::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getConsignee()), KxAddress::getConsignee, bo.getConsignee());
        lqw.eq(bo.getLongitude() != null, KxAddress::getLongitude, bo.getLongitude());
        lqw.eq(bo.getLatitude() != null, KxAddress::getLatitude, bo.getLatitude());
        return lqw;
    }

    /**
     * 新增用户下单地址
     */
    @Override
    public Boolean insertByBo(KxAddressBo bo) {
        Long addressNum = baseMapper.selectCount(new QueryWrapper<KxAddress>().eq("user_id", bo.getUserId()));
        KxAddress address = null;
        if (addressNum == 0) {
            address = BeanUtil.toBean(bo, KxAddress.class);
            address.setDefaultAddress(DefaultAddressStatusType.DEFAULT_ADDRESS.getCode());
        } else {
            if (bo.getDefaultAddress() != 0) {
                KxAddress preDefault = new KxAddress();
                preDefault.setDefaultAddress(0);
                if (!(baseMapper.update(preDefault //该用户有地址却没有默认地址，抛出该异常
                    , new QueryWrapper<KxAddress>()
                        .eq("user_id", bo.getUserId())
                        .eq("default_address", DefaultAddressStatusType.DEFAULT_ADDRESS.getCode())) > 0)) {
                    throw new ServiceException("地址更新失败！");
                }
                address = BeanUtil.toBean(bo, KxAddress.class);
                address.setDefaultAddress(DefaultAddressStatusType.DEFAULT_ADDRESS.getCode());
            } else {
                address = BeanUtil.toBean(bo, KxAddress.class);
                address.setDefaultAddress(DefaultAddressStatusType.COMMON_ADDRESS.getCode());
            }
        }
        Date now = new Date();
        address.setCreateTime(now);
        address.setUpdateTime(now);
        if (baseMapper.insert(address) > 0) {
            return true;
        } else {
            throw new ServiceException("地址更新失败！");
        }
    }

    /**
     * 修改用户下单地址
     */
    @Override
    public Boolean updateByBo(KxAddressBo bo) {
        KxAddress update = BeanUtil.toBean(bo, KxAddress.class);
        validEntityBeforeSave(update);
        Date now = new Date();
        if (bo.getDefaultAddress() != 0L) {
            bo.setDefaultAddress(DefaultAddressStatusType.DEFAULT_ADDRESS.getCode());
            List<KxAddress> addresses = baseMapper.selectList(new QueryWrapper<KxAddress>().eq("user_id", bo.getUserId()).eq("default_address", bo.getDefaultAddress()));
            if (addresses != null && addresses.size() != 0) {
                KxAddress preDefault = addresses.get(0);
                preDefault.setDefaultAddress(0);
                baseMapper.updateById(preDefault);
            }
        }
        update.setUpdateTime(now);
        return baseMapper.update(update, new QueryWrapper<KxAddress>()
            .eq("id", bo.getId())
            .eq("user_id", bo.getUserId())) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxAddress entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户下单地址
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public KxAddressVo getDefAddress(Long userId) {
       return baseMapper.selectVoOne(new LambdaQueryWrapper<KxAddress>()
            .eq(KxAddress::getUserId, userId)
            .eq(KxAddress::getDefaultAddress, 1));
    }

    @Override
    public Boolean deleteAddress(KxAddressBo bo) {
        return baseMapper.deleteById(bo.getId()) > 0;
    }
}
