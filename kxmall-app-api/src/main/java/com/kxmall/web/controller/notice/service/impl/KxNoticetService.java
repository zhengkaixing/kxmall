package com.kxmall.web.controller.notice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kxmall.system.domain.SysNotice;
import com.kxmall.system.mapper.SysNoticeMapper;
import com.kxmall.web.controller.notice.service.IKxNoticetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/8
 */

@RequiredArgsConstructor
@Service
public class KxNoticetService implements IKxNoticetService {

    private final SysNoticeMapper baseMapper;

    @Override
    public List<SysNotice> queryList(SysNotice bo) {
        return baseMapper.selectList(new LambdaQueryWrapper<SysNotice>().eq(SysNotice::getStatus, "0"));
    }
}
