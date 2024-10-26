package com.kxmall.web.controller.activity;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.kxmall.activity.domain.bo.KxStoreActivityBo;
import com.kxmall.activity.domain.vo.KxStoreActivityVo;
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
import com.kxmall.web.controller.activity.service.IKxStoreActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 活动商品
 *
 * @author kxmall
 * @date 2024-08-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/activity/storeActivity")
public class KxStoreActivityController extends BaseController {

    private final IKxStoreActivityService iKxStoreActivityService;

    /**
     * 查询活动商品列表
     */
    @SaCheckPermission("activity:storeActivity:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreActivityVo> list(KxStoreActivityBo bo, PageQuery pageQuery) {
        return iKxStoreActivityService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出活动商品列表
     */
    @SaCheckPermission("activity:storeActivity:export")
    @Log(title = "活动商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreActivityBo bo, HttpServletResponse response) {
        List<KxStoreActivityVo> list = iKxStoreActivityService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动商品", KxStoreActivityVo.class, response);
    }

    /**
     * 获取活动商品详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("activity:storeActivity:query")
    @GetMapping("/{id}")
    public R<KxStoreActivityVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iKxStoreActivityService.queryById(id));
    }

    /**
     * 新增活动商品
     */
    @SaCheckPermission("activity:storeActivity:add")
    @Log(title = "活动商品", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreActivityBo bo) {
        return toAjax(iKxStoreActivityService.insertByBo(bo));
    }

    /**
     * 修改活动商品
     */
    @SaCheckPermission("activity:storeActivity:edit")
    @Log(title = "活动商品", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreActivityBo bo) {
        return toAjax(iKxStoreActivityService.updateByBo(bo));
    }

    /**
     * 删除活动商品
     *
     * @param ids 主键串
     */
    @SaCheckPermission("activity:storeActivity:remove")
    @Log(title = "活动商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxStoreActivityService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
