package com.kxmall.web.controller.user.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.user.domain.vo.KxUserMemberLevelVo;
import com.kxmall.user.domain.vo.UserLevelDto;

import java.math.BigDecimal;
import java.util.List;

public interface IKxAppUserLevelService {

    UserLevelDto getLevelInfo(Long userId);

    boolean setLevelComplete(Long userId);

    Long getNextLevelId(Long levelId);

    KxUserMemberLevelVo queryById(Long id);

    TableDataInfo<KxUserMemberLevelVo> queryPageList(KxUserMemberLevelVo vo, PageQuery pageQuery);

    List<KxUserMemberLevelVo> queryList(KxUserMemberLevelVo vo);

    Boolean insertByBo(KxUserMemberLevelVo vo);

    Boolean updateByBo(KxUserMemberLevelVo vo);

    Boolean deleteWithValidByIds(List<Long> ids, Boolean isValid);

    BigDecimal setLevelPrice(BigDecimal price, Long uid);
}
