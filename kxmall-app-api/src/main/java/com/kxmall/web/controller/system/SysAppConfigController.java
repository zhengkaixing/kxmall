package com.kxmall.web.controller.system;

import cn.dev33.satoken.annotation.SaIgnore;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.R;
import com.kxmall.system.domain.SysConfig;
import com.kxmall.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author kxmall
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/app/config")
public class SysAppConfigController extends BaseAppController {

    private final ISysConfigService configService;


    /**
     * 根据分组参数键名查询参数值
     *
     * @param category 参数Key
     */
    @GetMapping(value = "/configKey/category/{category}")
    public R<List<SysConfig>> getConfigCategoryKey(@PathVariable String category) {
        return R.ok(configService.selectConfigCategoryByKey(category));
    }

    /**
     * 根据分组参数键名查询参数值
     *
     */
    @GetMapping(value = "/configKey/basicinfo")
    @SaIgnore
    public R<List<SysConfig>> getConfigCategoryBasicinfo() {
        String category = "basicinfo";
        return R.ok(configService.selectConfigCategoryByKey(category));
    }







}
