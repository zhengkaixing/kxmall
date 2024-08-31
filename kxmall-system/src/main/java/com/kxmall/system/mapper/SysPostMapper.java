package com.kxmall.system.mapper;

import com.kxmall.system.domain.SysPost;
import com.kxmall.common.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author 郅兴开源团队-小黑
 */
public interface SysPostMapper extends BaseMapperPlus<SysPostMapper, SysPost, SysPost> {

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    List<Long> selectPostListByUserId(Long userId);

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    List<SysPost> selectPostsByUserName(String userName);

}
