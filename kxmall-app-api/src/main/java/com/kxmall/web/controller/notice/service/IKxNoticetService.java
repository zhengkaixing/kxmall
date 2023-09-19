package com.kxmall.web.controller.notice.service;

import com.kxmall.system.domain.SysNotice;

import java.util.List;

/**
 * 公告Service接口
 *
 * @author kxmall
 * @date 2023-04-06
 */
public interface IKxNoticetService {


    /**
     * 查询公告
     */
    List<SysNotice> queryList(SysNotice bo);

}
