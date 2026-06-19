package com.kxmall.web.controller.user.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.user.domain.bo.KxUserSignBo;
import com.kxmall.user.domain.vo.KxUserSignVo;

import java.util.Collection;
import java.util.List;

/**
 * 签到记录Service接口
 *
 * @author kxmall
 * @date 2024-08-26
 */
public interface IKxUserSignService {

    KxUserSignVo queryById(Long id);

    TableDataInfo<KxUserSignVo> queryPageList(KxUserSignBo bo, PageQuery pageQuery);

    List<KxUserSignVo> queryList(KxUserSignBo bo);

    Boolean insertByBo(KxUserSignBo bo);

    Boolean updateByBo(KxUserSignBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
