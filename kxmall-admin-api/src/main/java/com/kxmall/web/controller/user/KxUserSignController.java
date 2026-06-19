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
import com.kxmall.user.domain.bo.KxUserSignBo;
import com.kxmall.user.domain.vo.KxUserSignVo;
import com.kxmall.web.controller.user.service.IKxUserSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 签到记录
 *
 * @author kxmall
 * @date 2024-08-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/userSign")
public class KxUserSignController extends BaseController {

    private final IKxUserSignService iKxUserSignService;

    @SaCheckPermission("user:userSign:list")
    @GetMapping("/list")
    public TableDataInfo<KxUserSignVo> list(KxUserSignBo bo, PageQuery pageQuery) {
        return iKxUserSignService.queryPageList(bo, pageQuery);
    }

    @SaCheckPermission("user:userSign:export")
    @Log(title = "签到记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxUserSignBo bo, HttpServletResponse response) {
        List<KxUserSignVo> list = iKxUserSignService.queryList(bo);
        ExcelUtil.exportExcel(list, "签到记录", KxUserSignVo.class, response);
    }

    @SaCheckPermission("user:userSign:query")
    @GetMapping("/{id}")
    public R<KxUserSignVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iKxUserSignService.queryById(id));
    }

    @SaCheckPermission("user:userSign:add")
    @Log(title = "签到记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxUserSignBo bo) {
        return toAjax(iKxUserSignService.insertByBo(bo));
    }

    @SaCheckPermission("user:userSign:edit")
    @Log(title = "签到记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxUserSignBo bo) {
        return toAjax(iKxUserSignService.updateByBo(bo));
    }

    @SaCheckPermission("user:userSign:remove")
    @Log(title = "签到记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(iKxUserSignService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
