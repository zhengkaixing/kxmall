package com.kxmall.web.controller.region;

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
import com.kxmall.region.domain.bo.KxRegionBo;
import com.kxmall.region.domain.vo.KxRegionVo;
import com.kxmall.web.controller.region.service.IKxRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 中国地区信息
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/region/region")
public class KxRegionController extends BaseController {

    private final IKxRegionService iKxRegionService;

    /**
     * 查询中国地区信息列表
     */
    @SaCheckPermission("region:region:list")
    @GetMapping("/list")
    public TableDataInfo<KxRegionVo> list(KxRegionBo bo, PageQuery pageQuery) {
        return iKxRegionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出中国地区信息列表
     */
    @SaCheckPermission("region:region:export")
    @Log(title = "中国地区信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxRegionBo bo, HttpServletResponse response) {
        List<KxRegionVo> list = iKxRegionService.queryList(bo);
        ExcelUtil.exportExcel(list, "中国地区信息", KxRegionVo.class, response);
    }

    /**
     * 获取中国地区信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("region:region:query")
    @GetMapping("/{id}")
    public R<KxRegionVo> getInfo(@NotNull(message = "主键不能为空")
                                 @PathVariable Long id) {
        return R.ok(iKxRegionService.queryById(id));
    }

    /**
     * 新增中国地区信息
     */
    @SaCheckPermission("region:region:add")
    @Log(title = "中国地区信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxRegionBo bo) {
        return toAjax(iKxRegionService.insertByBo(bo));
    }

    /**
     * 修改中国地区信息
     */
    @SaCheckPermission("region:region:edit")
    @Log(title = "中国地区信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxRegionBo bo) {
        return toAjax(iKxRegionService.updateByBo(bo));
    }

    /**
     * 删除中国地区信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("region:region:remove")
    @Log(title = "中国地区信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxRegionService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     *获取所有省份
     * @return
     */
    @GetMapping("/getProvinceAll")
    public List<Map<String, Object>> getProvinceAll()  {
        return iKxRegionService.getProvinceAll();
    }

    /**
     *获取所有城市
     * @return
     */
    @GetMapping("/getCityAll")
    public List<Map<String, Object>> getCityAll()  {
        return iKxRegionService.getCityAll();
    }

    /**
     *获取所有区(县)
     * @return
     */
    @GetMapping("/getCountyAll")
    public List<Map<String, Object>> getCountyAll()  {
        return iKxRegionService.getCountyAll();
    }


    /**
     *根据省份主键获取城市
     * @return
     */
    @GetMapping("/getCity")
    public List<Map<String, Object>> getCity(Long provinceId)  {
        return iKxRegionService.getCity(provinceId);
    }

    /**
     *根据城市主键获取区(县)
     * @return
     */
    @GetMapping("/getCounty")
    public List<Map<String, Object>> getCounty(Long cityId)  {
        return iKxRegionService.getCounty(cityId);
    }



}
