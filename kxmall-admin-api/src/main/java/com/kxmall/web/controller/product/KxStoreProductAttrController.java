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
import com.kxmall.product.domain.vo.KxStoreProductAttrVo;
import com.kxmall.product.domain.bo.KxStoreProductAttrBo;
import com.kxmall.web.controller.product.service.IKxStoreProductAttrService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 商品属性
 *
 * @author kxmall
 * @date 2023-02-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/storeProductAttr")
public class KxStoreProductAttrController extends BaseController {

    private final IKxStoreProductAttrService iWmStoreProductAttrService;

    /**
     * 查询商品属性列表
     */
    @SaCheckPermission("product:storeProductAttr:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreProductAttrVo> list(KxStoreProductAttrBo bo, PageQuery pageQuery) {
        return iWmStoreProductAttrService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品属性列表
     */
    @SaCheckPermission("product:storeProductAttr:export")
    @Log(title = "商品属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreProductAttrBo bo, HttpServletResponse response) {
        List<KxStoreProductAttrVo> list = iWmStoreProductAttrService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品属性", KxStoreProductAttrVo.class, response);
    }

    /**
     * 获取商品属性详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("product:storeProductAttr:query")
    @GetMapping("/{id}")
    public R<KxStoreProductAttrVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmStoreProductAttrService.queryById(id));
    }

    /**
     * 新增商品属性
     */
    @SaCheckPermission("product:storeProductAttr:add")
    @Log(title = "商品属性", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreProductAttrBo bo) {
        return toAjax(iWmStoreProductAttrService.insertByBo(bo));
    }

    /**
     * 修改商品属性
     */
    @SaCheckPermission("product:storeProductAttr:edit")
    @Log(title = "商品属性", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreProductAttrBo bo) {
        return toAjax(iWmStoreProductAttrService.updateByBo(bo));
    }

    /**
     * 删除商品属性
     *
     * @param ids 主键串
     */
    @SaCheckPermission("product:storeProductAttr:remove")
    @Log(title = "商品属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmStoreProductAttrService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
