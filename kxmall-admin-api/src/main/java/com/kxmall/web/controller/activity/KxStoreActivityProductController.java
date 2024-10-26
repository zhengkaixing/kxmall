package com.kxmall.web.controller.activity;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.activity.domain.KxStoreActivity;
import com.kxmall.activity.domain.KxStoreActivityProduct;
import com.kxmall.activity.domain.bo.KxStoreActivityProductBo;
import com.kxmall.activity.domain.vo.KxStoreActivityProductVo;
import com.kxmall.activity.mapper.KxStoreActivityMapper;
import com.kxmall.activity.mapper.KxStoreActivityProductMapper;
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
import com.kxmall.web.controller.activity.service.IKxStoreActivityProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/activity/storeActivityProduct")
public class KxStoreActivityProductController extends BaseController {

    private final IKxStoreActivityProductService iKxStoreActivityProductService;

    @Autowired
    KxStoreActivityMapper kxStoreActivityMapper;

    @Autowired
    KxStoreActivityProductMapper kxStoreActivityProductMapper;


    /**
     * 更新当前价格
     */
    @PostMapping("/updatePrice")
    public R<Boolean> updatePrice(@RequestBody KxStoreActivityProductBo bo){
        return R.ok(iKxStoreActivityProductService.updatePrice(bo));
    }


    /**
     * 获取推荐类型信息
     */
    @GetMapping("/listActivityType")
    public R<List<KxStoreActivity>> listActivityType() {
        List<KxStoreActivity> kxStoreActivities = kxStoreActivityMapper.selectList();
        return R.ok(kxStoreActivities);

    }

    /**
     * 批量新增推荐管理
     */
    @PostMapping("/addProductBatch")
    public R<Void> addProductBatch(@Validated(AddGroup.class) @RequestBody KxStoreActivityProductBo bo) {
        return toAjax(iKxStoreActivityProductService.addProductBatch(bo));
    }







    /**
     * 查询活动商品列表
     */
    @SaCheckPermission("activity:storeActivityProduct:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreActivityProductVo> list(KxStoreActivityProductBo bo, PageQuery pageQuery) {

        QueryWrapper<KxStoreActivityProduct> wrapper = Wrappers.query();
        wrapper.eq(bo.getActivityId() != null,"sap.activity_id", bo.getActivityId());
        Page<KxStoreActivityProductVo> kxStoreActivityProductVoPage = kxStoreActivityProductMapper.selectVoPageBySQL(pageQuery.build(), wrapper);
        return TableDataInfo.build(kxStoreActivityProductVoPage);
    }

    /**
     * 导出活动商品列表
     */
    @SaCheckPermission("activity:storeActivityProduct:export")
    @Log(title = "活动商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreActivityProductBo bo, HttpServletResponse response) {
        List<KxStoreActivityProductVo> list = iKxStoreActivityProductService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动商品", KxStoreActivityProductVo.class, response);
    }

    /**
     * 获取活动商品详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("activity:storeActivityProduct:query")
    @GetMapping("/{id}")
    public R<KxStoreActivityProductVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iKxStoreActivityProductService.queryById(id));
    }

    /**
     * 新增活动商品
     */
    @SaCheckPermission("activity:storeActivityProduct:add")
    @Log(title = "活动商品", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreActivityProductBo bo) {
        return toAjax(iKxStoreActivityProductService.insertByBo(bo));
    }

    /**
     * 修改活动商品
     */
    @SaCheckPermission("activity:storeActivityProduct:edit")
    @Log(title = "活动商品", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreActivityProductBo bo) {
        return toAjax(iKxStoreActivityProductService.updateByBo(bo));
    }

    /**
     * 删除活动商品
     *
     * @param ids 主键串
     */
    @SaCheckPermission("activity:storeActivityProduct:remove")
    @Log(title = "活动商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxStoreActivityProductService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
