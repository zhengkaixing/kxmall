package com.kxmall.web.controller.carousel;

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
import com.kxmall.carousel.domain.vo.KxCarouselVo;
import com.kxmall.carousel.domain.bo.KxCarouselBo;
import com.kxmall.web.controller.carousel.service.IKxCarouselService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 商铺广告
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/carousel/carousel")
public class KxCarouselController extends BaseController {

    private final IKxCarouselService iKxCarouselService;

    /**
     * 查询商铺广告列表
     */
    @SaCheckPermission("carousel:carousel:list")
    @GetMapping("/list")
    public TableDataInfo<KxCarouselVo> list(KxCarouselBo bo, PageQuery pageQuery) {
        return iKxCarouselService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商铺广告列表
     */
    @SaCheckPermission("carousel:carousel:export")
    @Log(title = "商铺广告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxCarouselBo bo, HttpServletResponse response) {
        List<KxCarouselVo> list = iKxCarouselService.queryList(bo);
        ExcelUtil.exportExcel(list, "商铺广告", KxCarouselVo.class, response);
    }

    /**
     * 获取商铺广告详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("carousel:carousel:query")
    @GetMapping("/{id}")
    public R<KxCarouselVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iKxCarouselService.queryById(id));
    }

    /**
     * 新增商铺广告
     */
    @SaCheckPermission("carousel:carousel:add")
    @Log(title = "商铺广告", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxCarouselBo bo) {
        return toAjax(iKxCarouselService.insertByBo(bo));
    }

    /**
     * 修改商铺广告
     */
    @SaCheckPermission("carousel:carousel:edit")
    @Log(title = "商铺广告", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxCarouselBo bo) {
        return toAjax(iKxCarouselService.updateByBo(bo));
    }

    /**
     * 删除商铺广告
     *
     * @param ids 主键串
     */
    @SaCheckPermission("carousel:carousel:remove")
    @Log(title = "商铺广告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxCarouselService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
