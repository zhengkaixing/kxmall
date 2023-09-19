package com.kxmall.web.controller.user;

import java.util.List;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.kxmall.common.annotation.RepeatSubmit;
import com.kxmall.common.annotation.Log;
import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import com.kxmall.common.enums.BusinessType;
import com.kxmall.common.utils.poi.ExcelUtil;
import com.kxmall.user.domain.vo.KxUserLevelSettingVo;
import com.kxmall.user.domain.bo.KxUserLevelSettingBo;
import com.kxmall.web.controller.user.service.IKxUserLevelSettingService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 设置用户等级
 *
 * @author kxmall
 * @date 2023-02-21
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/userLevelSetting")
public class KxUserLevelSettingController extends BaseController {

    private final IKxUserLevelSettingService iWmUserLevelSettingService;

    /**
     * 查询设置用户等级列表
     */
    @SaCheckPermission("user:userLevelSetting:list")
    @GetMapping("/list")
    public TableDataInfo<KxUserLevelSettingVo> list(KxUserLevelSettingBo bo, PageQuery pageQuery) {
        return iWmUserLevelSettingService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出设置用户等级列表
     */
    @SaCheckPermission("user:userLevelSetting:export")
    @Log(title = "设置用户等级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxUserLevelSettingBo bo, HttpServletResponse response) {
        List<KxUserLevelSettingVo> list = iWmUserLevelSettingService.queryList(bo);
        ExcelUtil.exportExcel(list, "设置用户等级", KxUserLevelSettingVo.class, response);
    }

    /**
     * 获取设置用户等级详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("user:userLevelSetting:query")
    @GetMapping("/{id}")
    public R<KxUserLevelSettingVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmUserLevelSettingService.queryById(id));
    }

    /**
     * 新增设置用户等级
     */
    @SaCheckPermission("user:userLevelSetting:add")
    @Log(title = "设置用户等级", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxUserLevelSettingBo bo) {
        return toAjax(iWmUserLevelSettingService.insertByBo(bo));
    }

    /**
     * 修改设置用户等级
     */
    @SaCheckPermission("user:userLevelSetting:edit")
    @Log(title = "设置用户等级", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxUserLevelSettingBo bo) {
        return toAjax(iWmUserLevelSettingService.updateByBo(bo));
    }

    /**
     * 删除设置用户等级
     *
     * @param ids 主键串
     */
    @SaCheckPermission("user:userLevelSetting:remove")
    @Log(title = "设置用户等级", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmUserLevelSettingService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
