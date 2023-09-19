package com.kxmall.storage.mapper;

import com.kxmall.storage.domain.KxStorage;
import com.kxmall.storage.domain.vo.KxStorageVo;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仓库管理Mapper接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface KxStorageMapper extends BaseMapperPlus<KxStorageMapper, KxStorage, KxStorageVo> {


    /**
     * 批量更新前置仓库资料状态
     *
     * @param ids   前置仓库主键集合
     * @param state 前置仓库资料状态
     * @return 影响行数
     */
    Integer batchUpdateState(@Param("ids") List<Long> ids, @Param("state") int state);

    /**
     * 批量更新前置仓库资料营业状态
     *
     * @param ids            前置仓库主键集合
     * @param operatingState 前置仓库资料营业状态
     * @return 影响行数
     */
    Integer batchUpdateOperatingState(@Param("ids") List<Long> ids, @Param("operatingState") int operatingState);

    /**
     * 获取所有仓库的名称
     * @param state
     * @param storageIds
     * @return
     */
    List<KxStorageVo> getStorageNameAll(int state, @Param("storageIds") List<Long> storageIds);

}
