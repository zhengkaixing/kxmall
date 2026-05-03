package com.kxmall.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kxmall.user.domain.KxUserTaskFinish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户任务完成记录Mapper接口
 *
 * @author kxmall
 * @date 2023-08-08
 */
@Mapper
public interface KxUserTaskFinishMapper extends BaseMapper<KxUserTaskFinish> {

    /**
     * 统计用户完成的任务数量
     *
     * @param taskIds 任务ID列表
     * @param uid 用户ID
     * @return 完成的任务数量
     */
    Long countByTaskIdsAndUid(List<Long> taskIds, Long uid);
}

