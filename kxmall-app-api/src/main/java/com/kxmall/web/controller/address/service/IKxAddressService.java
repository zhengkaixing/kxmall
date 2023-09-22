package com.kxmall.web.controller.address.service;

import com.kxmall.address.domain.bo.KxAddressBo;
import com.kxmall.address.domain.vo.KxAddressVo;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 用户下单地址Service接口
 *
 * @author kxmall
 * @date 2023-04-06
 */
public interface IKxAddressService {

    /**
     * 查询用户下单地址
     */
    KxAddressVo queryById(Long id);

    /**
     * 查询用户下单地址列表
     */
    TableDataInfo<KxAddressVo> queryPageList(KxAddressBo bo, PageQuery pageQuery);

    /**
     * 查询用户下单地址列表
     */
    List<KxAddressVo> queryList(KxAddressBo bo);

    /**
     * 新增用户下单地址
     */
    Boolean insertByBo(KxAddressBo bo);

    /**
     * 修改用户下单地址
     */
    Boolean updateByBo(KxAddressBo bo);

    /**
     * 校验并批量删除用户下单地址信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取默认地址
     * @param userId
     * @return
     */
    KxAddressVo getDefAddress(Long userId);

    Boolean deleteAddress(KxAddressBo bo);
}
