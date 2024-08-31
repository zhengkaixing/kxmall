package com.kxmall.system.service;

import com.alibaba.fastjson.JSONObject;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.system.domain.SysConfig;

import java.util.List;

/**
 * 参数配置 服务层
 *
 * @author 郅兴开源团队-小黑
 */
public interface ISysConfigService {


    TableDataInfo<SysConfig> selectPageConfigList(SysConfig config, PageQuery pageQuery);

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    SysConfig selectConfigById(Long configId);

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    String selectConfigByKey(String configKey);


    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    String selectConfigByKeyNoCache(String configKey);

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    boolean selectCaptchaEnabled();

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    List<SysConfig> selectConfigList(SysConfig config);

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    String insertConfig(SysConfig config);

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    String updateConfig(SysConfig config);

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    void deleteConfigByIds(Long[] configIds);

    /**
     * 加载参数缓存数据
     */
    void loadingConfigCache();

    /**
     * 清空参数缓存数据
     */
    void clearConfigCache();

    /**
     * 重置参数缓存数据
     */
    void resetConfigCache();

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数信息
     * @return 结果
     */
    String checkConfigKeyUnique(SysConfig config);

    /**
     * 保存指定参数（自定义）
     * @param jsonStr
     */
    List<SysConfig> saveConfig(String category,JSONObject jsonObject);

    /**
     * 查询分组下参数
     * @param category
     * @return
     */
    List<SysConfig> selectConfigCategoryByKey(String category);

}
