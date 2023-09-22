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
import com.kxmall.storage.domain.vo.KxGoodsInStockVo;
import com.kxmall.storage.domain.bo.KxGoodsInStockBo;
import com.kxmall.web.controller.storage.service.IKxGoodsInStockService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 商品入库
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage/goodsInStock")
public class KxGoodsInStockController extends BaseController {

    private final IKxGoodsInStockService iKxGoodsInStockService;

    /**
     * 查询商品入库列表
     */
    @SaCheckPermission("storage:goodsInStock:list")
    @GetMapping("/list")
    public TableDataInfo<KxGoodsInStockVo> list(KxGoodsInStockBo bo, PageQuery pageQuery) {
        return iKxGoodsInStockService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品入库列表
     */
    @SaCheckPermission("storage:goodsInStock:export")
    @Log(title = "商品入库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxGoodsInStockBo bo, HttpServletResponse response) {
        List<KxGoodsInStockVo> list = iKxGoodsInStockService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品入库", KxGoodsInStockVo.class, response);
    }

    /**
     * 获取商品入库详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("storage:goodsInStock:query")
    @GetMapping("/{id}")
    public R<KxGoodsInStockVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iKxGoodsInStockService.queryById(id));
    }

    /**
     * 新增商品入库
     */
    @SaCheckPermission("storage:goodsInStock:add")
    @Log(title = "商品入库", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxGoodsInStockBo bo) {
        return toAjax(iKxGoodsInStockService.insertByBo(bo));
    }

    /**
     * 修改商品入库
     */
    @SaCheckPermission("storage:goodsInStock:edit")
    @Log(title = "商品入库", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxGoodsInStockBo bo) {
        return toAjax(iKxGoodsInStockService.updateByBo(bo));
    }

    /**
     * 删除商品入库
     *
     * @param ids 主键串
     */
    @SaCheckPermission("storage:goodsInStock:remove")
    @Log(title = "商品入库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxGoodsInStockService.deleteWithValidByIds(Arrays.asList(ids), true));
    }



    /**
     * 入库
     */
    @RepeatSubmit()
    @PostMapping("/updateInOfStock")
    public R<Boolean> updateInOfStock(@Validated(AddGroup.class) @RequestBody KxGoodsInStockBo bo) {
        return R.ok(iKxGoodsInStockService.updateInOfStock(bo));
    }


    /**
     * 获取所有仓库的名称
     */
    @RepeatSubmit()
    @PostMapping("/storagAllName")
    public R<List<KxStorageVo>> storagAllName(@Validated(AddGroup.class) @RequestBody KxGoodsInStockBo bo) {
        return R.ok(iKxGoodsInStockService.storagAllName(bo));
    }

}
