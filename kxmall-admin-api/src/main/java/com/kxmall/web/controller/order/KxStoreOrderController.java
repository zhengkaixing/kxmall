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
 * 订单
 *
 * @author kxmall
 * @date 2023-02-15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/order/storeOrder")
public class KxStoreOrderController extends BaseController {

    private final IKxStoreOrderService kxStoreOrderService;

    /**
     * 查询订单列表
     */
    @SaCheckPermission("order:storeOrder:list")
    @GetMapping("/list")
    public TableDataInfo<KxStoreOrderVo> list(KxStoreOrderBo bo, PageQuery pageQuery) {
        return kxStoreOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出订单列表
     */
    @SaCheckPermission("order:storeOrder:export")
    @Log(title = "订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxStoreOrderBo bo, HttpServletResponse response) {
        List<KxStoreOrderVo> list = kxStoreOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "订单", KxStoreOrderVo.class, response);
    }

    /**
     * 获取订单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("order:storeOrder:query")
    @GetMapping("/{id}")
    public R<KxStoreOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kxStoreOrderService.queryById(id));
    }


    /**
     * 新增订单
     */
    @SaCheckPermission("order:storeOrder:add")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxStoreOrderBo bo) {
        return toAjax(kxStoreOrderService.insertByBo(bo));
    }

    /**
     * 修改订单
     */
    @SaCheckPermission("order:storeOrder:edit")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxStoreOrderBo bo) {
        if (StrUtil.isBlank(bo.getDeliveryName())) {
            throw new ServiceException("请选择快递公司");
        }
        if (StrUtil.isBlank(bo.getDeliveryId())) {
            throw new ServiceException("快递单号不能为空");
        }
        return toAjax(kxStoreOrderService.updateByBo(bo));
    }

    /**
     * 删除订单
     *
     * @param ids 主键串
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
     */
    @GetMapping(value = "/startStocking")
    public R<Boolean> startStocking(Long id) {
        return R.ok(kxStoreOrderService.startStocking(id));
    }


    /**
     * 完成配货
     */
    @GetMapping(value = "/completeAllocation")
    public R<Boolean> completeAllocation(Long id) {
        return R.ok(kxStoreOrderService.completeAllocation(id));
    }


    /**
     * 商家自配
     */
    @GetMapping(value = "/merchantDistribution")
    public R<Boolean> merchantDistribution(Long id) {
        return R.ok(kxStoreOrderService.merchantDistribution(id));
    }


    /**
     * 完成配送
     */
    @GetMapping(value = "/completeDelivery")
    public R<Boolean> completeDelivery(Long id) {
        return R.ok(kxStoreOrderService.completeDelivery(id));
    }


}
