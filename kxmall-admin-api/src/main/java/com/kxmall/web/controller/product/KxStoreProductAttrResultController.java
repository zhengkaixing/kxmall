package com.kxmall.web.controller.product;

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
import com.kxmall.product.domain.vo.KxStoreProductAttrResultVo;
import com.kxmall.product.domain.bo.KxStoreProductAttrResultBo;
import com.kxmall.web.controller.product.service.IKxStoreProductAttrResultService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 商品属性详情
 *
 * @author kxmall
 * @date 2023-02-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/storeProductAttrResult")
public class KxStoreProductAttrResultController extends BaseController {

    private final IKxStoreProductAttrResultService iWmStoreProductAttrResultService;

    /**
     * 查询商品属性详情列表
     */
    @SaCheckPermission("product:storeProductAttrResult:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreProductAttrResultVo> list(KxStoreProductAttrResultBo bo, PageQuery pageQuery) {
        return iWmStoreProductAttrResultService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品属性详情列表
     */
    @SaCheckPermission("product:storeProductAttrResult:export")
    @Log(title = "商品属性详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreProductAttrResultBo bo, HttpServletResponse response) {
        List<KxStoreProductAttrResultVo> list = iWmStoreProductAttrResultService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品属性详情", KxStoreProductAttrResultVo.class, response);
    }

    /**
     * 获取商品属性详情详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("product:storeProductAttrResult:query")
    @GetMapping("/{id}")
    public R<KxStoreProductAttrResultVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmStoreProductAttrResultService.queryById(id));
    }

    /**
     * 新增商品属性详情
     */
    @SaCheckPermission("product:storeProductAttrResult:add")
    @Log(title = "商品属性详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreProductAttrResultBo bo) {
        return toAjax(iWmStoreProductAttrResultService.insertByBo(bo));
    }

    /**
     * 修改商品属性详情
     */
    @SaCheckPermission("product:storeProductAttrResult:edit")
    @Log(title = "商品属性详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreProductAttrResultBo bo) {
        return toAjax(iWmStoreProductAttrResultService.updateByBo(bo));
    }

    /**
     * 删除商品属性详情
     *
     * @param ids 主键串
     */
    @SaCheckPermission("product:storeProductAttrResult:remove")
    @Log(title = "商品属性详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmStoreProductAttrResultService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
