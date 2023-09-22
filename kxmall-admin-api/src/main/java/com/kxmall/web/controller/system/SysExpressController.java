package com.kxmall.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.kxmall.common.annotation.Log;
import com.kxmall.common.annotation.RepeatSubmit;
import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import com.kxmall.common.enums.BusinessType;
import com.kxmall.common.utils.poi.ExcelUtil;
import com.kxmall.system.domain.bo.SysExpressBo;
import com.kxmall.system.domain.vo.SysExpressVo;
import com.kxmall.web.controller.system.service.ISysExpressService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 快递公司
 *
 * @author kxmall
 * @date 2023-02-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/express")
public class SysExpressController extends BaseController {

    private final ISysExpressService iSysExpressService;

    /**
     * 查询快递公司列表
     */
    @SaCheckPermission("system:express:list")
    @GetMapping("/list")
    public TableDataInfo<SysExpressVo> list(SysExpressBo bo, PageQuery pageQuery) {
        return iSysExpressService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出快递公司列表
     */
    @SaCheckPermission("system:express:export")
    @Log(title = "快递公司", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysExpressBo bo, HttpServletResponse response) {
        List<SysExpressVo> list = iSysExpressService.queryList(bo);
        ExcelUtil.exportExcel(list, "快递公司", SysExpressVo.class, response);
    }

    /**
     * 获取快递公司详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:express:query")
    @GetMapping("/{id}")
    public R<SysExpressVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iSysExpressService.queryById(id));
    }

    /**
     * 新增快递公司
     */
    @SaCheckPermission("system:express:add")
    @Log(title = "快递公司", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysExpressBo bo) {
        return toAjax(iSysExpressService.insertByBo(bo));
    }

    /**
     * 修改快递公司
     */
    @SaCheckPermission("system:express:edit")
    @Log(title = "快递公司", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysExpressBo bo) {
        return toAjax(iSysExpressService.updateByBo(bo));
    }

    /**
     * 删除快递公司
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:express:remove")
    @Log(title = "快递公司", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iSysExpressService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
