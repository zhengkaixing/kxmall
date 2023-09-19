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
import com.kxmall.user.domain.vo.KxUserBillVo;
import com.kxmall.user.domain.bo.KxUserBillBo;
import com.kxmall.web.controller.user.service.IKxUserBillService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 用户账单
 *
 * @author kxmall
 * @date 2023-02-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/userBill")
public class KxUserBillController extends BaseController {

    private final IKxUserBillService iWmUserBillService;

    /**
     * 查询用户账单列表
     */
    @SaCheckPermission("user:userBill:list")
    @GetMapping("/list")
    public TableDataInfo<KxUserBillVo> list(KxUserBillBo bo, PageQuery pageQuery) {
        return iWmUserBillService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户账单列表
     */
    @SaCheckPermission("user:userBill:export")
    @Log(title = "用户账单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxUserBillBo bo, HttpServletResponse response) {
        List<KxUserBillVo> list = iWmUserBillService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户账单", KxUserBillVo.class, response);
    }

    /**
     * 获取用户账单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("user:userBill:query")
    @GetMapping("/{id}")
    public R<KxUserBillVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmUserBillService.queryById(id));
    }

    /**
     * 新增用户账单
     */
    @SaCheckPermission("user:userBill:add")
    @Log(title = "用户账单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxUserBillBo bo) {
        return toAjax(iWmUserBillService.insertByBo(bo));
    }

    /**
     * 修改用户账单
     */
    @SaCheckPermission("user:userBill:edit")
    @Log(title = "用户账单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxUserBillBo bo) {
        return toAjax(iWmUserBillService.updateByBo(bo));
    }

    /**
     * 删除用户账单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("user:userBill:remove")
    @Log(title = "用户账单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmUserBillService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
