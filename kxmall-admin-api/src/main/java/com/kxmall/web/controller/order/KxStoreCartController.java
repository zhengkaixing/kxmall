package com.kxmall.web.controller.order;

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
import com.kxmall.order.domain.vo.KxStoreCartVo;
import com.kxmall.order.domain.bo.KxStoreCartBo;
import com.kxmall.web.controller.order.service.IKxStoreCartService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 购物车
 *
 * @author kxmall
 * @date 2023-02-15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/order/storeCart")
public class KxStoreCartController extends BaseController {

    private final IKxStoreCartService kxStoreCartService;

    /**
     * 查询购物车列表
     */
    @SaCheckPermission("order:storeCart:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreCartVo> list(KxStoreCartBo bo, PageQuery pageQuery) {
        return kxStoreCartService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出购物车列表
     */
    @SaCheckPermission("order:storeCart:export")
    @Log(title = "购物车", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreCartBo bo, HttpServletResponse response) {
        List<KxStoreCartVo> list = kxStoreCartService.queryList(bo);
        ExcelUtil.exportExcel(list, "购物车", KxStoreCartVo.class, response);
    }

    /**
     * 获取购物车详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("order:storeCart:query")
    @GetMapping("/{id}")
    public R<KxStoreCartVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kxStoreCartService.queryById(id));
    }

    /**
     * 新增购物车
     */
    @SaCheckPermission("order:storeCart:add")
    @Log(title = "购物车", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreCartBo bo) {
        return toAjax(kxStoreCartService.insertByBo(bo));
    }

    /**
     * 修改购物车
     */
    @SaCheckPermission("order:storeCart:edit")
    @Log(title = "购物车", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreCartBo bo) {
        return toAjax(kxStoreCartService.updateByBo(bo));
    }

    /**
     * 删除购物车
     *
     * @param ids 主键串
     */
    @SaCheckPermission("order:storeCart:remove")
    @Log(title = "购物车", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kxStoreCartService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
