package com.kxmall.web.controller.user;

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
import com.kxmall.user.domain.bo.KxUserTaskBo;
import com.kxmall.user.domain.vo.KxUserTaskVo;
import com.kxmall.web.controller.user.service.IKxUserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 等级任务设置
 *
 * @author kxmall
 * @date 2023-08-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/userTask")
public class KxUserTaskController extends BaseController {

    private final IKxUserTaskService iKxUserTaskService;

    @SaCheckPermission("user:userTask:list")
    @GetMapping("/list")
    public TableDataInfo<KxUserTaskVo> list(KxUserTaskBo bo, PageQuery pageQuery) {
        return iKxUserTaskService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("user:userTask:export")
    @Log(title = "等级任务设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxUserTaskBo bo, HttpServletResponse response) {
        List<KxUserTaskVo> list = iKxUserTaskService.queryList(bo);
        ExcelUtil.exportExcel(list, "等级任务设置", KxUserTaskVo.class, response);
    }

    @SaCheckPermission("user:userTask:query")
    @GetMapping("/{id}")
    public R<KxUserTaskVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iKxUserTaskService.queryById(id));
    }

    @SaCheckPermission("user:userTask:add")
    @Log(title = "等级任务设置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxUserTaskBo bo) {
        return toAjax(iKxUserTaskService.insertByBo(bo));
    }

    @SaCheckPermission("user:userTask:edit")
    @Log(title = "等级任务设置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxUserTaskBo bo) {
        return toAjax(iKxUserTaskService.updateByBo(bo));
    }

    @SaCheckPermission("user:userTask:remove")
    @Log(title = "等级任务设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(iKxUserTaskService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
