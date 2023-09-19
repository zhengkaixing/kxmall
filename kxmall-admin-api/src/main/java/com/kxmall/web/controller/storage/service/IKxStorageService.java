package com.kxmall.web.controller.storage.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.storage.domain.bo.KxStorageBo;
import com.kxmall.storage.domain.vo.KxStorageVo;

import java.util.Collection;
import java.util.List;

/**
 * 仓库管理Service接口
 *
 * @author kxmall
 * @date 2023-08-27
 */
public interface IKxStorageService {

    /**
     * 查询仓库管理
     */
    KxStorageVo queryById(Long id);

    /**
     * 查询仓库管理列表
     */
    TableDataInfo<KxStorageVo> queryPageList(KxStorageBo bo, PageQuery pageQuery);

    /**
     * 查询仓库管理列表
     */
    List<KxStorageVo> queryList(KxStorageBo bo);

    /**
     * 新增仓库管理
     */
    Boolean insertByBo(KxStorageBo bo);

    /**
     * 修改仓库管理
     */
    Boolean updateByBo(KxStorageBo bo);

    /**
     * 校验并批量删除仓库管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 前置仓状态批量更新为正常
     * @param bo
     * @return
     */
    Boolean updateStateToNomral(KxStorageBo bo);

    Boolean updateStateToAbort(KxStorageBo bo);

    /**
     * 前置仓营业状态批量更新为营业中
     * @param bo
     * @return
     */
    Boolean updateBusinessStateToOpen(KxStorageBo bo);

    /**
     * 前置仓营业状态批量更新为休息中
     * @param bo
     * @return
     */
    Boolean updateBusinessStateToRest(KxStorageBo bo);

    List<KxStorageVo> options();

    /**
     * 获取指定仓库的推送订阅二维码
     * @return
     */
    String getStorageQrcodeImage();

    Boolean printTest(KxStorageBo bo);
}
