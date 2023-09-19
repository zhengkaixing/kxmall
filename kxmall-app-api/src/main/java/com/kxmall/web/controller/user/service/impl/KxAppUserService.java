package com.kxmall.web.controller.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kxmall.user.domain.KxUser;
import com.kxmall.user.domain.bo.KxUserBo;
import com.kxmall.user.domain.vo.KxUserVo;
import com.kxmall.user.mapper.KxUserMapper;
import com.kxmall.web.controller.user.service.IKxAppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 足迹Service业务层处理
 *
 * @author kxmall
 * @date 2023-04-06
 */
@RequiredArgsConstructor
@Service
public class KxAppUserService implements IKxAppUserService {

    private final KxUserMapper baseMapper;

    private static final String VERIFY_CODE_PREFIX = "VERIFY_CODE_";

    /**
     * 查询足迹
     */
    @Override
    public KxUserVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 修改足迹
     */
    @Override
    public Boolean updateByBo(KxUserBo bo) {
        KxUser update = BeanUtil.toBean(bo, KxUser.class);
        return baseMapper.updateById(update) > 0;
    }


}
