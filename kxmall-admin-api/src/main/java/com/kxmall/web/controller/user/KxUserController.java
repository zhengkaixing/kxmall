package com.kxmall.web.controller.user;

import java.util.List;
import java.util.Arrays;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kxmall.user.domain.vo.KxPromUserVo;
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
import com.kxmall.user.domain.vo.KxUserVo;
import com.kxmall.user.domain.bo.KxUserBo;
import com.kxmall.web.controller.user.service.IKxUserService;
import com.kxmall.common.core.page.TableDataInfo;

/**
 * 用户
 *
 * @author kxmall
 * @date 2023-02-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/user")
public class KxUserController extends BaseController {

    private final IKxUserService iWmUserService;

    /**
     * 查询用户列表
     */
    @SaCheckPermission("user:user:list")
    @GetMapping("/list")
    public TableDataInfo<KxUserVo> list(KxUserBo bo, PageQuery pageQuery) {
        if ("nickname".equals(bo.getType())) {
            bo.setNickname(bo.getValue());
        }
        if ("phone".equals(bo.getType())) {
            bo.setPhone(bo.getValue());
        }
        return iWmUserService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户列表
     */
    @SaCheckPermission("user:user:export")
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(KxUserBo bo, HttpServletResponse response) {
        List<KxUserVo> list = iWmUserService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户", KxUserVo.class, response);
    }

    /**
     * 获取用户详细信息
     *
     * @param uid 主键
     */
    @SaCheckPermission("user:user:query")
    @GetMapping("/{uid}")
    public R<KxUserVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long uid) {
        return R.ok(iWmUserService.queryById(uid));
    }

    /**
     * 新增用户
     */
    @SaCheckPermission("user:user:add")
    @Log(title = "用户", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody KxUserBo bo) {
        return toAjax(iWmUserService.insertByBo(bo));
    }

    /**
     * 修改用户
     */
    @SaCheckPermission("user:user:edit")
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody KxUserBo bo) {
        return toAjax(iWmUserService.updateByBo(bo));
    }

    /**
     * 删除用户
     *
     * @param uids 主键串
     */
    @SaCheckPermission("user:user:remove")
    @Log(title = "用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{uids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] uids) {
        return toAjax(iWmUserService.deleteWithValidByIds(Arrays.asList(uids), true));
    }


    /**
     * 查看下级
     * @return
     */
    @PostMapping(value = "/spread")
    @SaCheckPermission("user:user:query")
    public R<List<KxPromUserVo>> getSpread(@RequestBody KxUserBo bo){
        return R.ok(iWmUserService.querySpread(bo));
    }



    /**
     * 更改状态
     */
    @SaCheckPermission("user:user:edit")
    @PostMapping(value = "/onStatus/{id}")
    public R<Void> onStatus(@PathVariable Long id, @RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Integer status = jsonObject.getInteger("status");
        iWmUserService.onStatus(id,status);
        return R.ok();
    }

    /**
     * 修改余额
     */
    @RepeatSubmit()
    @PostMapping(value = "/money")
    @SaCheckPermission("user:user:edit")
    public R<Void> updatePrice(@Validated @RequestBody KxUserBo param){
        iWmUserService.updateMoney(param);
        return R.ok();
    }

}
