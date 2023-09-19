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
import com.kxmall.user.domain.vo.KxUserLevelVo;
import com.kxmall.user.domain.bo.KxUserLevelBo;
import com.kxmall.web.controller.user.service.IKxUserLevelService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 用户等级
 *
 * @author kxmall
 * @date 2023-02-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/userLevel")
public class KxUserLevelController extends BaseController {

    private final IKxUserLevelService iWmUserLevelService;

    /**
     * 查询用户等级列表
     */
    @SaCheckPermission("user:userLevel:list")
    @GetMapping("/list")
    public TableDataInfo<KxUserLevelVo> list(KxUserLevelBo bo, PageQuery pageQuery) {
        return iWmUserLevelService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户等级列表
     */
    @SaCheckPermission("user:userLevel:export")
    @Log(title = "用户等级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxUserLevelBo bo, HttpServletResponse response) {
        List<KxUserLevelVo> list = iWmUserLevelService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户等级", KxUserLevelVo.class, response);
    }

    /**
     * 获取用户等级详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("user:userLevel:query")
    @GetMapping("/{id}")
    public R<KxUserLevelVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmUserLevelService.queryById(id));
    }

    /**
     * 新增用户等级
     */
    @SaCheckPermission("user:userLevel:add")
    @Log(title = "用户等级", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxUserLevelBo bo) {
        return toAjax(iWmUserLevelService.insertByBo(bo));
    }

    /**
     * 修改用户等级
     */
    @SaCheckPermission("user:userLevel:edit")
    @Log(title = "用户等级", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxUserLevelBo bo) {
        return toAjax(iWmUserLevelService.updateByBo(bo));
    }

    /**
     * 删除用户等级
     *
     * @param ids 主键串
     */
    @SaCheckPermission("user:userLevel:remove")
    @Log(title = "用户等级", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmUserLevelService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
