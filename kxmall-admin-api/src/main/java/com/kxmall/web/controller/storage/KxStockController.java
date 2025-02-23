package com.kxmall.web.controller.storage;

import cn.dev33.satoken.annotation.SaCheckPermission;
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
import com.kxmall.storage.domain.bo.KxStockBo;
import com.kxmall.storage.domain.bo.WarningStockBo;
import com.kxmall.storage.domain.vo.KxStockVo;
import com.kxmall.web.controller.storage.service.IKxStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 前置仓商品
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage/stock")
public class KxStockController extends BaseController {

    private final IKxStockService iKxStockService;

    /**
     * 查询前置仓商品列表
     */
    @SaCheckPermission("storage:stock:list")
    @GetMapping("/list")
    public TableDataInfo<KxStockVo> list(KxStockBo bo, PageQuery pageQuery) {
        bo.setStorageIds(getLoginUser().getStoragePermission());
        return iKxStockService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出前置仓商品列表
     */
    @SaCheckPermission("storage:stock:export")
    @Log(title = "前置仓商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStockBo bo, HttpServletResponse response) {
        List<KxStockVo> list = iKxStockService.queryList(bo);
        ExcelUtil.exportExcel(list, "前置仓商品", KxStockVo.class, response);
    }

    /**
     * 获取前置仓商品详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("storage:stock:query")
    @GetMapping("/{id}")
    public R<KxStockVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iKxStockService.queryById(id));
    }

    /**
     * 新增前置仓商品
     */
    @SaCheckPermission("storage:stock:add")
    @Log(title = "前置仓商品", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStockBo bo) {
        return toAjax(iKxStockService.insertByBo(bo));
    }

    /**
     * 修改前置仓商品
     */
    @SaCheckPermission("storage:stock:edit")
    @Log(title = "前置仓商品", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStockBo bo) {
        return toAjax(iKxStockService.updateByBo(bo));
    }

    /**
     * 删除前置仓商品
     *
     * @param ids 主键串
     */
    @SaCheckPermission("storage:stock:remove")
    @Log(title = "前置仓商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxStockService.deleteWithValidByIds(Arrays.asList(ids), true));
    }


    /**
     * 上下架
     */
    @PostMapping("/freezeOrActivation")
    public R<Boolean> freezeOrActivation(@RequestBody KxStockBo bo){
        return R.ok(iKxStockService.freezeOrActivation(bo));
    }


    /**
     * 逻辑删除
     */
    @PostMapping("/updateByStock")
    public R<Boolean> updateByStock(@RequestBody KxStockBo bo){
        return R.ok(iKxStockService.updateByStock(bo));
    }


    /**
     * 更新当前价格
     */
    @PostMapping("/updatePrice")
    public R<Boolean> updatePrice(@RequestBody KxStockBo bo){
        return R.ok(iKxStockService.updatePrice(bo));
    }


    /**
     * 库存预警列表
     */
    @GetMapping("/warningList")
    public TableDataInfo<KxStockVo> warningList(WarningStockBo bo, PageQuery pageQuery) {
        bo.setStorageIds(getLoginUser().getStoragePermission());
        return iKxStockService.queryPageWarningList(bo, pageQuery);
    }


    /**
     * 设置预警量
     */
    @PostMapping("/warningUpdate")
    public R<Boolean> warningUpdate(@RequestBody WarningStockBo bo) {
        return R.ok(iKxStockService.warningUpdate(bo));
    }



    /**
     * 仓库二维码
     */
    @PostMapping("/warehouseCode")
    public R<String> warehouseCode(@RequestBody WarningStockBo bo) {
        return R.ok(iKxStockService.warehouseCode(bo));
    }
}
