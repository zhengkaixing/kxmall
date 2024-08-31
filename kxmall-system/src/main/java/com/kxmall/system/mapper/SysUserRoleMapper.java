package com.kxmall.system.mapper;

import com.kxmall.system.domain.SysUserRole;
import com.kxmall.common.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author 郅兴开源团队-小黑
 */
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRoleMapper, SysUserRole, SysUserRole> {

    List<Long> selectUserIdsByRoleId(Long roleId);

}
