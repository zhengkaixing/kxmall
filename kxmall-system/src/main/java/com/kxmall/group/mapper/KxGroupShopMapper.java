package com.kxmall.group.mapper;

import com.kxmall.common.core.mapper.BaseMapperPlus;
import com.kxmall.group.domain.KxGroupShop;
import com.kxmall.group.domain.vo.KxGroupShopVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 团购Mapper接口
 *
 * @author kxmall
 * @date 2023-10-07
 */
public interface KxGroupShopMapper extends BaseMapperPlus<KxGroupShopMapper, KxGroupShop, KxGroupShopVo> {


    List<KxGroupShopVo> getGroupShopPage(@Param("storageId") Long storageId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    KxGroupShopVo detail(@Param("groupShopId") Long groupShopId, @Param("storageId") Long storageId);

    Integer incCurrentNum(@Param("id") Long id,@Param("num") Integer num);
}
