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
import com.kxmall.product.domain.vo.KxStoreProductAttrValueVo;
import com.kxmall.product.domain.bo.KxStoreProductAttrValueBo;
import com.kxmall.web.controller.product.service.IKxStoreProductAttrValueService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 商品属性值
 *
 * @author kxmall
 * @date 2023-02-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/storeProductAttrValue")
public class KxStoreProductAttrValueController extends BaseController {

    private final IKxStoreProductAttrValueService iWmStoreProductAttrValueService;

    /**
     * 查询商品属性值列表
     */
    @SaCheckPermission("product:storeProductAttrValue:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreProductAttrValueVo> list(KxStoreProductAttrValueBo bo, PageQuery pageQuery) {
        return iWmStoreProductAttrValueService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品属性值列表
     */
    @SaCheckPermission("product:storeProductAttrValue:export")
    @Log(title = "商品属性值", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreProductAttrValueBo bo, HttpServletResponse response) {
        List<KxStoreProductAttrValueVo> list = iWmStoreProductAttrValueService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品属性值", KxStoreProductAttrValueVo.class, response);
    }

    /**
     * 获取商品属性值详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("product:storeProductAttrValue:query")
    @GetMapping("/{id}")
    public R<KxStoreProductAttrValueVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmStoreProductAttrValueService.queryById(id));
    }

    /**
     * 新增商品属性值
     */
    @SaCheckPermission("product:storeProductAttrValue:add")
    @Log(title = "商品属性值", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreProductAttrValueBo bo) {
        return toAjax(iWmStoreProductAttrValueService.insertByBo(bo));
    }

    /**
     * 修改商品属性值
     */
    @SaCheckPermission("product:storeProductAttrValue:edit")
    @Log(title = "商品属性值", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreProductAttrValueBo bo) {
        return toAjax(iWmStoreProductAttrValueService.updateByBo(bo));
    }

    /**
     * 删除商品属性值
     *
     * @param ids 主键串
     */
    @SaCheckPermission("product:storeProductAttrValue:remove")
    @Log(title = "商品属性值", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmStoreProductAttrValueService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
