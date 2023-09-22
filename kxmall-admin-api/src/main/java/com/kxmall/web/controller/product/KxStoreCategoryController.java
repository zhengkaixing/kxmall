package com.kxmall.web.controller.product;

import java.util.List;
import java.util.Arrays;
import java.util.Map;

import com.kxmall.common.exception.ServiceException;
import com.kxmall.web.controller.product.service.IKxStoreProductService;
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
import com.kxmall.product.domain.vo.KxStoreCategoryVo;
import com.kxmall.product.domain.bo.KxStoreCategoryBo;
import com.kxmall.web.controller.product.service.IKxStoreCategoryService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 商品分类
 *
 * @author kxmall
 * @date 2023-02-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/storeCategory")
public class KxStoreCategoryController extends BaseController {

    private final IKxStoreCategoryService iWmStoreCategoryService;

    private final IKxStoreProductService iWmStoreProductService;

    /**
     * 查询商品分类列表
     */
    @SaCheckPermission("product:storeCategory:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreCategoryVo> list(KxStoreCategoryBo bo, PageQuery pageQuery) {
        return iWmStoreCategoryService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询商品分类列表(树形)
     */
    @SaCheckPermission("product:storeCategory:list")
    @GetMapping("/listTree")
    public R<Map<String,Object>> list(KxStoreCategoryBo bo) {
        List<KxStoreCategoryVo> storeCategoryVos = iWmStoreCategoryService.queryList(bo);
        Map<String,Object> result = iWmStoreCategoryService.buildTree(storeCategoryVos);
        return R.ok(result);
    }

    /**
     * 导出商品分类列表
     */
    @SaCheckPermission("product:storeCategory:export")
    @Log(title = "商品分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreCategoryBo bo, HttpServletResponse response) {
        List<KxStoreCategoryVo> list = iWmStoreCategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品分类", KxStoreCategoryVo.class, response);
    }

    /**
     * 获取商品分类详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("product:storeCategory:query")
    @GetMapping("/{id}")
    public R<KxStoreCategoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmStoreCategoryService.queryById(id));
    }

    /**
     * 新增商品分类
     */
    @SaCheckPermission("product:storeCategory:add")
    @Log(title = "商品分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreCategoryBo bo) {
//        if(bo.getPid() != null && bo.getPid() > 0 && StrUtil.isBlank(bo.getPic())) {
//            throw new ServiceException("子分类图片必传");
//        }

        boolean checkResult = iWmStoreCategoryService.checkCategory(bo.getPid());
        if(!checkResult) {
            throw new ServiceException("分类最多能添加2级哦");
        }
        return toAjax(iWmStoreCategoryService.insertByBo(bo));
    }

    /**
     * 修改商品分类
     */
    @SaCheckPermission("product:storeCategory:edit")
    @Log(title = "商品分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreCategoryBo bo) {

//        if(bo.getPid() != null && bo.getPid() > 0 && StrUtil.isBlank(bo.getPic())) {
//            throw new ServiceException("子分类图片必传");
//        }

        if(bo.getId().equals(bo.getPid())){
            throw new ServiceException("自己不能选择自己哦");
        }

        boolean checkResult = iWmStoreCategoryService.checkCategory(bo.getPid());

        if(!checkResult) {
            throw new ServiceException("分类最多能添加2级哦");
        }

        return toAjax(iWmStoreCategoryService.updateByBo(bo));
    }

    /**
     * 删除商品分类
     *
     * @param ids 主键串
     */
    @SaCheckPermission("product:storeCategory:remove")
    @Log(title = "商品分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        for (Long newId: ids) {
            this.delCheck(newId);
        }
        return toAjax(iWmStoreCategoryService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 检测删除分类
     * @param id 分类id
     */
    private void delCheck(Long id){
        Long count = iWmStoreCategoryService.selectCountByPid(id);
        if(count > 0) {
            throw new ServiceException("请先删除子分类");
        }

        Long countP = iWmStoreProductService.selectCountByCateId(id);

        if(countP > 0) {
            throw new ServiceException("当前分类下有商品不可删除");
        }
    }

}
