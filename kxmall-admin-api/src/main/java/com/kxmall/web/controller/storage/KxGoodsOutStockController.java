package com.kxmall.web.controller.storage;

import java.util.List;
import java.util.Arrays;

import com.kxmall.storage.domain.vo.KxStorageVo;
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
import com.kxmall.storage.domain.vo.KxGoodsOutStockVo;
import com.kxmall.storage.domain.bo.KxGoodsOutStockBo;
import com.kxmall.web.controller.storage.service.IKxGoodsOutStockService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 商品出库
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage/goodsOutStock")
public class KxGoodsOutStockController extends BaseController {

    private final IKxGoodsOutStockService iKxGoodsOutStockService;

    /**
     * 查询商品出库列表
     */
    @SaCheckPermission("storage:goodsOutStock:list")
    @GetMapping("/list")
    public TableDataInfo<KxGoodsOutStockVo> list(KxGoodsOutStockBo bo, PageQuery pageQuery) {
        return iKxGoodsOutStockService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品出库列表
     */
    @SaCheckPermission("storage:goodsOutStock:export")
    @Log(title = "商品出库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxGoodsOutStockBo bo, HttpServletResponse response) {
        List<KxGoodsOutStockVo> list = iKxGoodsOutStockService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品出库", KxGoodsOutStockVo.class, response);
    }

    /**
     * 获取商品出库详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("storage:goodsOutStock:query")
    @GetMapping("/{id}")
    public R<KxGoodsOutStockVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iKxGoodsOutStockService.queryById(id));
    }

    /**
     * 新增商品出库
     */
    @SaCheckPermission("storage:goodsOutStock:add")
    @Log(title = "商品出库", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxGoodsOutStockBo bo) {
        return toAjax(iKxGoodsOutStockService.insertByBo(bo));
    }

    /**
     * 修改商品出库
     */
    @SaCheckPermission("storage:goodsOutStock:edit")
    @Log(title = "商品出库", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxGoodsOutStockBo bo) {
        return toAjax(iKxGoodsOutStockService.updateByBo(bo));
    }

    /**
     * 删除商品出库
     *
     * @param ids 主键串
     */
    @SaCheckPermission("storage:goodsOutStock:remove")
    @Log(title = "商品出库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxGoodsOutStockService.deleteWithValidByIds(Arrays.asList(ids), true));
    }


    /**
     * 入库
     */
    @RepeatSubmit()
    @PostMapping("/updateOutOfStock")
    public R<Boolean> updateOutOfStock(@Validated(AddGroup.class) @RequestBody KxGoodsOutStockBo bo) {
        return R.ok(iKxGoodsOutStockService.updateOutOfStock(bo));
    }


    /**
     * 获取所有仓库的名称
     */
    @RepeatSubmit()
    @PostMapping("/storagAllName")
    public R<List<KxStorageVo>> storagAllName(@Validated(AddGroup.class) @RequestBody KxGoodsOutStockBo bo) {
        return R.ok(iKxGoodsOutStockService.storagAllName(bo));
    }
}
