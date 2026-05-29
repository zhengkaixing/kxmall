package com.kxmall.web.controller.seckill;

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
import com.kxmall.seckill.domain.bo.KxStoreSeckillBo;
import com.kxmall.seckill.domain.vo.KxStoreSeckillVo;
import com.kxmall.web.controller.seckill.service.IKxStoreSeckillService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 商品秒杀
 *
 * @author kxmall
 * @date 2024-05-15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/seckill/storeSeckill")
public class KxStoreSeckillController extends BaseController {

    private final IKxStoreSeckillService iKxStoreSeckillService;

    /**
     * 查询商品秒杀列表
     */
    @SaCheckPermission("seckill:storeSeckill:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreSeckillVo> list(KxStoreSeckillBo bo, PageQuery pageQuery) {
        return iKxStoreSeckillService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品秒杀列表
     */
    @SaCheckPermission("seckill:storeSeckill:export")
    @Log(title = "商品秒杀", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreSeckillBo bo, HttpServletResponse response) {
        List<KxStoreSeckillVo> list = iKxStoreSeckillService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品秒杀", KxStoreSeckillVo.class, response);
    }

    /**
     * 获取商品秒杀详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("seckill:storeSeckill:query")
    @GetMapping("/{id}")
    public R<KxStoreSeckillVo> getInfo(@NotNull(message = "主键不能为空")
                                       @PathVariable Long id) {
        return R.ok(iKxStoreSeckillService.queryById(id));
    }

    /**
     * 新增商品秒杀
     */
    @SaCheckPermission("seckill:storeSeckill:add")
    @Log(title = "商品秒杀", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreSeckillBo bo) {
        return toAjax(iKxStoreSeckillService.insertByBo(bo));
    }

    /**
     * 修改商品秒杀
     */
    @SaCheckPermission("seckill:storeSeckill:edit")
    @Log(title = "商品秒杀", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreSeckillBo bo) {
        return toAjax(iKxStoreSeckillService.updateByBo(bo));
    }

    /**
     * 删除商品秒杀
     *
     * @param ids 主键串
     */
    @SaCheckPermission("seckill:storeSeckill:remove")
    @Log(title = "商品秒杀", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxStoreSeckillService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
