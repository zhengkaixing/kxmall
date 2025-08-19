package com.kxmall.web.controller.dish;

import com.kxmall.common.annotation.Log;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.BusinessType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.dish.domain.bo.CompositionDishDTO;
import com.kxmall.dish.domain.bo.DishSpuDTO;
import com.kxmall.web.controller.dish.service.CompositionDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 套餐菜品
 * @author kxmall
 * @date 2021-01-05
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/dish/composition/app")
public class AppCompositionDishController extends BaseAppController {

    private final CompositionDishService compositionDishService;

    /**
     * 分页查询套餐菜品列表
     *
     * @param storageId  仓库ID（可选参数），可能用于根据仓库库存过滤套餐
     * @param pageQuery  分页查询参数，包含页码、每页数量等信息
     * @return TableDataInfo<CompositionDishDTO> 分页数据响应，包含列表和分页信息
     * @throws ServiceException 业务异常
     */
    @GetMapping("/list")
    public TableDataInfo<CompositionDishDTO> list(
            @RequestParam(required = false) Long storageId, // 请求参数，非必传
            PageQuery pageQuery) throws ServiceException {
        // 调用Service层方法获取分页列表
        return compositionDishService.list(storageId, pageQuery);
    }

    /**
     * 根据ID查询套餐菜品详情
     *
     * @param id 套餐菜品的主键ID (路径变量)
     * @return R<DishSpuDTO> 通用响应体，包含套餐详情数据
     * @throws ServiceException 业务异常
     */
    @GetMapping("/{id}")
    public R<DishSpuDTO> queryById(
            @PathVariable @NotNull Long id) throws ServiceException { // 从路径中获取必传的id参数
        // 从上下文中获取当前登录APP用户的ID
        Long userId = getAppLoginUser().getUserId();
        // 调用Service层方法查询详情，并返回成功响应
        return R.ok(compositionDishService.queryById(id, userId));
    }

    /**
     * 增加指定套餐菜品的销量
     *
     * @param id 套餐菜品的主键ID (路径变量)
     * @return R<String> 通用响应体，通常返回操作结果提示信息
     * @throws ServiceException 业务异常
     */
    @Log(title = "增加销量", businessType = BusinessType.UPDATE)
    @PostMapping("/addSale/{id}")
    public R<String> addSale(@PathVariable @NotNull Long id) throws ServiceException {
        // 调用Service层方法增加销量，并返回成功响应及消息
        return R.ok(compositionDishService.addSale(id));
    }
}