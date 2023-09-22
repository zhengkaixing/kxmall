package com.kxmall.web.controller.address;

import com.kxmall.address.domain.bo.KxAddressBo;
import com.kxmall.address.domain.vo.KxAddressVo;
import com.kxmall.common.annotation.RepeatSubmit;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import com.kxmall.common.utils.poi.ExcelUtil;
import com.kxmall.web.controller.address.service.IKxAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 用户下单地址
 *
 * @author kxmall
 * @date 2023-04-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/address/app")
public class KxAddressController extends BaseAppController {

    private final IKxAddressService iKxAddressService;

    /**
     * 查询用户下单地址列表
     */
    @GetMapping("/list")
    public TableDataInfo<KxAddressVo> list(KxAddressBo bo, PageQuery pageQuery) {
        return iKxAddressService.queryPageList(bo, pageQuery);
    }


    /**
     * 查询用户下单地址列表
     */
    @GetMapping("/getAllAddress")
    public R<List<KxAddressVo>> getAllAddress() {
        KxAddressBo bo = new KxAddressBo();
        bo.setUserId(getAppLoginUser().getUserId());
        return R.ok(iKxAddressService.queryList(bo));
    }

    /**
     * 获取用户下单地址详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<KxAddressVo> getInfo(@NotNull(message = "主键不能为空")
                                  @PathVariable Long id) {
        return R.ok(iKxAddressService.queryById(id));
    }



    /**
     * 获取默认地址
     */
    @GetMapping("/getDefAddress")
    public R<KxAddressVo> getDefAddress() {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(iKxAddressService.getDefAddress(userId));
    }

    /**
     * 新增用户下单地址
     */
    @RepeatSubmit()
    @PostMapping("/addAddress")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxAddressBo bo) {
        bo.setUserId(getAppLoginUser().getUserId());
        return toAjax(iKxAddressService.insertByBo(bo));
    }

    /**
     * 修改用户下单地址
     */
    @RepeatSubmit()
    @PostMapping("/updateAddress")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxAddressBo bo) {
        bo.setUserId(getAppLoginUser().getUserId());
        return toAjax(iKxAddressService.updateByBo(bo));
    }

    /**
     * 删除地址
     */
    @RepeatSubmit()
    @PostMapping("/deleteAddress")
    public R<Void> deleteAddress(@Validated(EditGroup.class) @RequestBody KxAddressBo bo) {
        bo.setUserId(getAppLoginUser().getUserId());
        return toAjax(iKxAddressService.deleteAddress(bo));
    }

    /**
     * 删除用户下单地址
     *
     * @param ids 主键串
     */
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxAddressService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
