package com.kxmall.web.controller.storage;

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
import com.kxmall.storage.domain.vo.KxStorageVo;
import com.kxmall.storage.domain.bo.KxStorageBo;
import com.kxmall.web.controller.storage.service.IKxStorageService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 仓库管理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage/storage")
public class KxStorageController extends BaseController {

    private final IKxStorageService iKxStorageService;

    /**
     * 查询仓库管理列表
     */
    @SaCheckPermission("storage:storage:list")
    @GetMapping("/list")
    public TableDataInfo<KxStorageVo> list(KxStorageBo bo, PageQuery pageQuery) {
        return iKxStorageService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询仓库管理列表（全部）
     */
    @GetMapping("/listAll")
    public R<List<KxStorageVo>> listAll(KxStorageBo bo) {
        return R.ok(iKxStorageService.queryList(bo));
    }

    /**
     * 导出仓库管理列表
     */
    @SaCheckPermission("storage:storage:export")
    @Log(title = "仓库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStorageBo bo, HttpServletResponse response) {
        List<KxStorageVo> list = iKxStorageService.queryList(bo);
        ExcelUtil.exportExcel(list, "仓库管理", KxStorageVo.class, response);
    }

    /**
     * 获取仓库管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("storage:storage:query")
    @GetMapping("/{id}")
    public R<KxStorageVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iKxStorageService.queryById(id));
    }

    /**
     * 新增仓库管理
     */
    @SaCheckPermission("storage:storage:add")
    @Log(title = "仓库管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStorageBo bo) {
        return toAjax(iKxStorageService.insertByBo(bo));
    }

    /**
     * 修改仓库管理
     */
    @SaCheckPermission("storage:storage:edit")
    @Log(title = "仓库管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStorageBo bo) {
        return toAjax(iKxStorageService.updateByBo(bo));
    }

    /**
     * 删除仓库管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("storage:storage:remove")
    @Log(title = "仓库管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxStorageService.deleteWithValidByIds(Arrays.asList(ids), true));
    }



    /**
     * 前置仓状态批量更新为正常
     */
    @PostMapping("/updateStateToNomral")
    public R<Boolean> updateStateToNomral(@RequestBody KxStorageBo bo){
       return R.ok(iKxStorageService.updateStateToNomral(bo));
    }


    /**
     * 前置仓状态批量更新为禁用
     */
    @PostMapping("/updateStateToAbort")
    public R<Boolean> updateStateToAbort(@RequestBody KxStorageBo bo){
       return R.ok(iKxStorageService.updateStateToAbort(bo));
    }



    /**
     * 前置仓营业状态批量更新为营业中
     */
    @PostMapping("/updateBusinessStateToOpen")
    public R<Boolean> updateBusinessStateToOpen(@RequestBody KxStorageBo bo){
        return R.ok(iKxStorageService.updateBusinessStateToOpen(bo));
    }



    /**
     * 前置仓营业状态批量更新为休息中
     */
    @PostMapping("/updateBusinessStateToRest")
    public R<Boolean> updateBusinessStateToRest(@RequestBody KxStorageBo bo){
        return R.ok(iKxStorageService.updateBusinessStateToRest(bo));
    }



    /**
     * 前置仓营业状态批量更新为休息中
     */
    @PostMapping("/options")
    public R<List<KxStorageVo>> options(){
        return R.ok(iKxStorageService.options());
    }



    /**
     * 获取指定仓库的推送订阅二维码
     */
    @PostMapping("/getStorageQrcodeImage")
    public R<String> getStorageQrcodeImage(){
        return R.ok(iKxStorageService.getStorageQrcodeImage());
    }


    /**
     * 打印测试
     */
    @PostMapping("/printTest")
    public R<Boolean> printTest(@RequestBody KxStorageBo bo){
        return R.ok(iKxStorageService.printTest(bo));
    }






}
