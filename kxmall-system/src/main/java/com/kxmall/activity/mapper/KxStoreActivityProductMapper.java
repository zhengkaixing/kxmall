package com.kxmall.activity.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.activity.domain.KxStoreActivityProduct;
import com.kxmall.activity.domain.vo.KxStoreActivityProductVo;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动商品Mapper接口
 *
 * @author kxmall
 * @date 2024-08-07
 */
public interface KxStoreActivityProductMapper extends BaseMapperPlus<KxStoreActivityProductMapper, KxStoreActivityProduct, KxStoreActivityProductVo> {

    Page<KxStoreActivityProductVo> selectVoPageBySQL(@Param("page") Page<KxStoreActivityProduct> page, @Param(Constants.WRAPPER) Wrapper<KxStoreActivityProduct> lqw);

    List<KxStoreActivityProductVo> getActivityProductByStorage(@Param("storageId") Long storageId,@Param("activityId")  String activityId,@Param("offset")  Integer offset,@Param("size") Integer size);

    Long getActivityProductByStorageCount(@Param("storageId") Long storageId,@Param("activityId")  String activityId);
}
