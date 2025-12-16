package com.kxmall.web.controller.order;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import com.kxmall.common.annotation.Log;
import com.kxmall.common.annotation.RepeatSubmit;
import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import com.kxmall.common.enums.BusinessType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.poi.ExcelUtil;
import com.kxmall.order.domain.bo.KxStoreOrderBo;
import com.kxmall.order.domain.vo.KxStoreOrderVo;
import com.kxmall.web.controller.order.service.IKxStoreOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 订单管理控制器
 * 提供订单的增删改查、导出、配货、配送等功能
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-02-15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/order/storeOrder")
public class KxStoreOrderController extends BaseController {

    /**
     * 订单服务接口
     */
    private final IKxStoreOrderService kxStoreOrderService;

    /**
     * 分页查询订单列表
     * 支持多条件筛选查询订单
     *
     * @param bo 订单查询条件对象
     * @param pageQuery 分页查询参数
     * @return TableDataInfo<KxStoreOrderVo> 分页订单数据
     */
    @SaCheckPermission("order:storeOrder:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreOrderVo> list(KxStoreOrderBo bo, PageQuery pageQuery) {
        return kxStoreOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出订单列表
     * 将订单数据导出为Excel文件，支持按条件筛选导出
     *
     * @param bo 订单查询条件对象
     * @param response HTTP响应对象
     */
    @SaCheckPermission("order:storeOrder:export")
    @Log(title = "订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreOrderBo bo, HttpServletResponse response) {
        // 查询订单列表
        List<KxStoreOrderVo> list = kxStoreOrderService.queryList(bo);
        // 导出Excel
        ExcelUtil.exportExcel(list, "订单", KxStoreOrderVo.class, response);
    }

    /**
     * 获取订单详细信息
     * 根据订单ID查询订单完整信息，包括商品明细、收货地址等
     *
     * @param id 订单主键ID（必填）
     * @return R<KxStoreOrderVo> 订单详细信息
     */
    @SaCheckPermission("order:storeOrder:query")
    @GetMapping("/{id}")
    public R<KxStoreOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                      @PathVariable Long id) {
        return R.ok(kxStoreOrderService.queryById(id));
    }

    /**
     * 新增订单
     * 管理员手动创建订单（通常用于补单等场景）
     *
     * @param bo 订单业务对象，包含订单所有信息
     * @return R<Void> 操作结果
     */
    @SaCheckPermission("order:storeOrder:add")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @RepeatSubmit() // 防止重复提交
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreOrderBo bo) {
        return toAjax(kxStoreOrderService.insertByBo(bo));
    }

    /**
     * 修改订单
     * 主要用于更新订单的物流信息（快递公司、快递单号）
     *
     * @param bo 订单业务对象，包含需要更新的字段
     * @return R<Void> 操作结果
     */
    @SaCheckPermission("order:storeOrder:edit")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @RepeatSubmit() // 防止重复提交
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreOrderBo bo) {
        // 校验快递公司
        if (StrUtil.isBlank(bo.getDeliveryName())) {
            throw new ServiceException("请选择快递公司");
        }
        // 校验快递单号
        if (StrUtil.isBlank(bo.getDeliveryId())) {
            throw new ServiceException("快递单号不能为空");
        }
        // 执行更新操作
        return toAjax(kxStoreOrderService.updateByBo(bo));
    }

    /**
     * 删除订单
     * 支持批量删除，删除前会进行有效性校验
     * 注意：删除订单需谨慎，建议使用软删除
     *
     * @param ids 订单主键ID数组（必填）
     * @return R<Void> 操作结果
     */
    @SaCheckPermission("order:storeOrder:remove")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kxStoreOrderService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 开始配货
     * 订单状态变更为配货中，开始准备商品
     *
     * @param id 订单主键ID
     * @return R<Boolean> 操作结果，true表示成功
     */
    @GetMapping(value = "/startStocking")
    public R<Boolean> startStocking(Long id) {
        return R.ok(kxStoreOrderService.startStocking(id));
    }

    /**
     * 完成配货
     * 订单配货完成，商品已准备好
     *
     * @param id 订单主键ID
     * @return R<Boolean> 操作结果，true表示成功
     */
    @GetMapping(value = "/completeAllocation")
    public R<Boolean> completeAllocation(Long id) {
        return R.ok(kxStoreOrderService.completeAllocation(id));
    }

    /**
     * 商家自配
     * 订单由商家自己配送，不通过第三方快递
     *
     * @param id 订单主键ID
     * @return R<Boolean> 操作结果，true表示成功
     */
    @GetMapping(value = "/merchantDistribution")
    public R<Boolean> merchantDistribution(Long id) {
        return R.ok(kxStoreOrderService.merchantDistribution(id));
    }

    /**
     * 完成配送
     * 订单配送完成，订单状态变更为已完成
     *
     * @param id 订单主键ID
     * @return R<Boolean> 操作结果，true表示成功
     */
    @GetMapping(value = "/completeDelivery")
    public R<Boolean> completeDelivery(Long id) {
        return R.ok(kxStoreOrderService.completeDelivery(id));
    }

}
