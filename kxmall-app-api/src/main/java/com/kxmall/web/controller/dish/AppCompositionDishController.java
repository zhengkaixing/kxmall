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

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/dish/composition/app")
public class AppCompositionDishController extends BaseAppController {

    private final CompositionDishService compositionDishService;

    @GetMapping("/list")
    public TableDataInfo<CompositionDishDTO> list(
            @RequestParam(required = false) Long storageId,
            PageQuery pageQuery) throws ServiceException {
        return compositionDishService.list(storageId, pageQuery);
    }

    @GetMapping("/{id}")
    public R<DishSpuDTO> queryById(
            @PathVariable @NotNull Long id) throws ServiceException {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(compositionDishService.queryById(id, userId));
    }

    @Log(title = "增加销量", businessType = BusinessType.UPDATE)
    @PostMapping("/addSale/{id}")
    public R<String> addSale(@PathVariable @NotNull Long id) throws ServiceException {
        return R.ok(compositionDishService.addSale(id));
    }
}