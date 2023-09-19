package com.kxmall.web.controller.recommend;

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
import com.kxmall.recommend.domain.vo.KxRecommendVo;
import com.kxmall.recommend.domain.bo.KxRecommendBo;
import com.kxmall.web.controller.recommend.service.IKxRecommendService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 推荐管理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/recommend/recommend")
public class KxRecommendController extends BaseController {

    private final IKxRecommendService iKxRecommendService;

    private final static String RECOMMEND_NAME = "RECOMMEND_TYPE_";

    /**
     * 查询推荐管理列表
     */
    @SaCheckPermission("recommend:recommend:list")
    @GetMapping("/list")
    public TableDataInfo<KxRecommendVo> list(KxRecommendBo bo, PageQuery pageQuery) {
        return iKxRecommendService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询推荐管理列表(全部)
     */
    @GetMapping("/listAll")
    public R<List<KxRecommendVo>> listAll(KxRecommendBo bo) {
        return R.ok(iKxRecommendService.queryList(bo));
    }

    /**
     * 导出推荐管理列表
     */
    @SaCheckPermission("recommend:recommend:export")
    @Log(title = "推荐管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxRecommendBo bo, HttpServletResponse response) {
        List<KxRecommendVo> list = iKxRecommendService.queryList(bo);
        ExcelUtil.exportExcel(list, "推荐管理", KxRecommendVo.class, response);
    }

    /**
     * 获取推荐管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("recommend:recommend:query")
    @GetMapping("/{id}")
    public R<KxRecommendVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iKxRecommendService.queryById(id));
    }

    /**
     * 新增推荐管理
     */
    @SaCheckPermission("recommend:recommend:add")
    @Log(title = "推荐管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxRecommendBo bo) {
        return toAjax(iKxRecommendService.insertByBo(bo));
    }

    /**
     * 批量新增推荐管理
     */
    @PostMapping("/addRecommendBatch")
    public R<Void> addRecommendBatch(@Validated(AddGroup.class) @RequestBody KxRecommendBo bo) {
        return toAjax(iKxRecommendService.addRecommendBatch(bo));
    }

    /**
     * 修改推荐管理
     */
    @SaCheckPermission("recommend:recommend:edit")
    @Log(title = "推荐管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxRecommendBo bo) {
        return toAjax(iKxRecommendService.updateByBo(bo));
    }

    /**
     * 删除推荐管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("recommend:recommend:remove")
    @Log(title = "推荐管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxRecommendService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
