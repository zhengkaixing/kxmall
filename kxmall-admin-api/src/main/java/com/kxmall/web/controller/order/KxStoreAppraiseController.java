package com.kxmall.order.controller;

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
import com.kxmall.order.domain.vo.KxStoreAppraiseVo;
import com.kxmall.order.domain.bo.KxStoreAppraiseBo;
import com.kxmall.web.controller.order.service.IKxStoreAppraiseService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 评论管理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/order/storeAppraise")
public class KxStoreAppraiseController extends BaseController {

    private final IKxStoreAppraiseService iKxStoreAppraiseService;

    /**
     * 查询评论管理列表
     */
    @SaCheckPermission("order:storeAppraise:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreAppraiseVo> list(KxStoreAppraiseBo bo, PageQuery pageQuery) {
        return iKxStoreAppraiseService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出评论管理列表
     */
    @SaCheckPermission("order:storeAppraise:export")
    @Log(title = "评论管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreAppraiseBo bo, HttpServletResponse response) {
        List<KxStoreAppraiseVo> list = iKxStoreAppraiseService.queryList(bo);
        ExcelUtil.exportExcel(list, "评论管理", KxStoreAppraiseVo.class, response);
    }

    /**
     * 获取评论管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("order:storeAppraise:query")
    @GetMapping("/{id}")
    public R<KxStoreAppraiseVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iKxStoreAppraiseService.queryById(id));
    }

    /**
     * 新增评论管理
     */
    @SaCheckPermission("order:storeAppraise:add")
    @Log(title = "评论管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreAppraiseBo bo) {
        return toAjax(iKxStoreAppraiseService.insertByBo(bo));
    }

    /**
     * 修改评论管理
     */
    @SaCheckPermission("order:storeAppraise:edit")
    @Log(title = "评论管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreAppraiseBo bo) {
        return toAjax(iKxStoreAppraiseService.updateByBo(bo));
    }

    /**
     * 删除评论管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("order:storeAppraise:remove")
    @Log(title = "评论管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxStoreAppraiseService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
