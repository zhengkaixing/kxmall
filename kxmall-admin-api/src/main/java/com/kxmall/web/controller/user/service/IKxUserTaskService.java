package com.kxmall.web.controller.user.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.user.domain.bo.KxUserTaskBo;
import com.kxmall.user.domain.vo.KxUserTaskVo;

import java.util.List;

public interface IKxUserTaskService {

    TableDataInfo<KxUserTaskVo> queryPageList(KxUserTaskBo bo, PageQuery pageQuery);

    List<KxUserTaskVo> queryList(KxUserTaskBo bo);

    KxUserTaskVo queryById(Long id);

    Boolean insertByBo(KxUserTaskBo bo);

    Boolean updateByBo(KxUserTaskBo bo);

    Boolean deleteWithValidByIds(List<Long> ids, Boolean isValid);
}
