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
import com.kxmall.product.domain.vo.KxStoreProductRuleVo;
import com.kxmall.product.domain.bo.KxStoreProductRuleBo;
import com.kxmall.web.controller.product.service.IKxStoreProductRuleService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 商品规格
 *
 * @author kxmall
 * @date 2023-02-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/storeProductRule")
public class KxStoreProductRuleController extends BaseController {

    private final IKxStoreProductRuleService iWmStoreProductRuleService;

    /**
     * 查询商品规格列表
     */
    @SaCheckPermission("product:storeProductRule:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreProductRuleVo> list(KxStoreProductRuleBo bo, PageQuery pageQuery) {
        return iWmStoreProductRuleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品规格列表
     */
    @SaCheckPermission("product:storeProductRule:export")
    @Log(title = "商品规格", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreProductRuleBo bo, HttpServletResponse response) {
        List<KxStoreProductRuleVo> list = iWmStoreProductRuleService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品规格", KxStoreProductRuleVo.class, response);
    }

    /**
     * 获取商品规格详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("product:storeProductRule:query")
    @GetMapping("/{id}")
    public R<KxStoreProductRuleVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmStoreProductRuleService.queryById(id));
    }

    /**
     * 新增商品规格
     */
    @SaCheckPermission("product:storeProductRule:add")
    @Log(title = "商品规格", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreProductRuleBo bo) {
        return toAjax(iWmStoreProductRuleService.insertByBo(bo));
    }

    /**
     * 修改商品规格
     */
    @SaCheckPermission("product:storeProductRule:edit")
    @Log(title = "商品规格", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreProductRuleBo bo) {
        return toAjax(iWmStoreProductRuleService.updateByBo(bo));
    }

    /**
     * 删除商品规格
     *
     * @param ids 主键串
     */
    @SaCheckPermission("product:storeProductRule:remove")
    @Log(title = "商品规格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Integer[] ids) {
        return toAjax(iWmStoreProductRuleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
