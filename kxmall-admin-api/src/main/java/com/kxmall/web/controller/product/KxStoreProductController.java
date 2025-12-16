package com.kxmall.web.controller.product;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kxmall.common.annotation.Log;
import com.kxmall.common.annotation.RepeatSubmit;
import com.kxmall.common.constant.ShopConstants;
import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import com.kxmall.common.enums.BusinessType;
import com.kxmall.common.utils.poi.ExcelUtil;
import com.kxmall.product.domain.bo.KxStoreProductBo;
import com.kxmall.product.domain.vo.KxStoreProductVo;
import com.kxmall.product.domain.vo.ProductTreeNodeVo;
import com.kxmall.web.controller.product.service.IKxStoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 商品管理控制器
 * 提供商品的增删改查、上架下架、属性生成、批量授权等功能
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-02-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/storeProduct")
public class KxStoreProductController extends BaseController {

    /**
     * 商品服务接口
     */
    private final IKxStoreProductService iWmStoreProductService;

    /**
     * 分页查询商品列表
     * 支持多条件筛选查询
     *
     * @param bo 商品查询条件对象
     * @param pageQuery 分页查询参数
     * @return TableDataInfo<KxStoreProductVo> 分页商品数据
     */
    @SaCheckPermission("product:storeProduct:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreProductVo> list(KxStoreProductBo bo, PageQuery pageQuery) {
        return iWmStoreProductService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品列表
     * 将商品数据导出为Excel文件
     *
     * @param bo 商品查询条件对象
     * @param response HTTP响应对象
     */
    @SaCheckPermission("product:storeProduct:export")
    @Log(title = "商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreProductBo bo, HttpServletResponse response) {
        // 查询商品列表
        List<KxStoreProductVo> list = iWmStoreProductService.queryList(bo);
        // 导出Excel
        ExcelUtil.exportExcel(list, "商品", KxStoreProductVo.class, response);
    }

    /**
     * 获取商品详细信息
     * 根据商品ID查询商品完整信息，包括属性、规格等
     *
     * @param id 商品主键ID（必填）
     * @return R<Map<String,Object>> 商品详细信息
     */
    @SaCheckPermission("product:storeProduct:query")
    @GetMapping("/{id}")
    public R<Map<String, Object>> getInfo(@NotNull(message = "主键不能为空")
                                         @PathVariable Long id) {
        return R.ok(iWmStoreProductService.queryById(id));
    }

    /**
     * 新增商品
     * 创建新的商品，包括商品基本信息、属性、规格等
     *
     * @param bo 商品业务对象，包含商品所有信息
     * @return R<Void> 操作结果
     */
    @SaCheckPermission("product:storeProduct:add")
    @Log(title = "商品", businessType = BusinessType.INSERT)
    @RepeatSubmit() // 防止重复提交
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreProductBo bo) {
        return toAjax(iWmStoreProductService.insertAndupdateByBo(bo));
    }

    /**
     * 修改商品
     * 更新已存在的商品信息
     *
     * @param bo 商品业务对象，包含需要更新的字段
     * @return R<Void> 操作结果
     */
    @SaCheckPermission("product:storeProduct:edit")
    @Log(title = "商品", businessType = BusinessType.UPDATE)
    @RepeatSubmit() // 防止重复提交
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreProductBo bo) {
        return toAjax(iWmStoreProductService.insertAndupdateByBo(bo));
    }

    /**
     * 删除商品
     * 支持批量删除，删除前会进行有效性校验
     *
     * @param ids 商品主键ID数组（必填）
     * @return R<Void> 操作结果
     */
    @SaCheckPermission("product:storeProduct:remove")
    @Log(title = "商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmStoreProductService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 商品上架/下架
     * 修改商品的销售状态，上架后商品可被购买，下架后商品不可见
     * 操作后会清除首页缓存
     *
     * @param id 商品主键ID
     * @param jsonStr JSON字符串，包含status字段（0-下架，1-上架）
     * @return R<Void> 操作结果
     */
    @CacheEvict(cacheNames = ShopConstants.WMHOP_REDIS_INDEX_KEY, allEntries = true)
    @PostMapping(value = "/onsale/{id}")
    public R<Void> onSale(@PathVariable Long id, @RequestBody String jsonStr) {
        // 解析JSON获取状态
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        // 执行上架/下架操作
        iWmStoreProductService.onSale(id, jsonObject.getInteger("status"));
        return R.ok();
    }

    /**
     * 生成商品属性
     * 根据商品ID和属性配置生成商品属性结构
     *
     * @param id 商品主键ID
     * @param jsonStr JSON字符串，包含属性配置信息
     * @return R<Map<String, Object>> 生成的属性结构
     */
    @PostMapping(value = "/isFormatAttr/{id}")
    public R<Map<String, Object>> formatAttr(@PathVariable Long id, @RequestBody String jsonStr) {
        return R.ok(iWmStoreProductService.getFormatAttr(id, jsonStr, false));
    }

    /**
     * 批量授权商品
     * 将商品批量授权给指定的仓库或店铺
     *
     * @param bo 商品业务对象，包含需要授权的商品ID和授权目标
     * @return R<Boolean> 授权结果，true表示成功
     */
    @PostMapping(value = "/batchAuthorizeGoods")
    public R<Boolean> batchAuthorizeGoods(@RequestBody KxStoreProductBo bo) {
        return R.ok(iWmStoreProductService.batchAuthorizeGoods(bo));
    }

    /**
     * 获取树形商品结构
     * 返回商品的树形结构数据，用于商品分类展示
     *
     * @return R<List<ProductTreeNodeVo>> 商品树形结构列表
     */
    @PostMapping(value = "/getProductBigTree")
    public R<List<ProductTreeNodeVo>> getProductBigTree() {
        return R.ok(iWmStoreProductService.getProductBigTree());
    }

}
