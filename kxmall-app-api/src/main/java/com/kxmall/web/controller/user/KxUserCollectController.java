package com.kxmall.web.controller.user;

import com.kxmall.common.annotation.RepeatSubmit;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.user.domain.bo.KxUserCollectBo;
import com.kxmall.user.domain.vo.KxUserCollectVo;
import com.kxmall.web.controller.user.service.IKxUserCollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 客户收藏
 *
 * @author kxmall
 * @date 2023-04-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/userCollect")
public class KxUserCollectController extends BaseAppController {

    private final IKxUserCollectService iKxUserCollectService;

    /**
     * 查询客户收藏列表
     */
    @GetMapping("/list")
    public TableDataInfo<KxUserCollectVo> list(KxUserCollectBo bo, PageQuery pageQuery) {
        return iKxUserCollectService.queryPageList(bo, pageQuery);
    }


    /**
     * 查询某一用户收藏记录
     */
    @GetMapping("/getCollectAll")
    public TableDataInfo<KxUserCollectVo> getCollectAll(KxUserCollectBo bo, PageQuery pageQuery) {
        Long userId = getAppLoginUser().getUserId();
        bo.setUserId(userId);
        return iKxUserCollectService.getCollectAll(bo,pageQuery);
    }

    /**
     * 获取客户收藏详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<KxUserCollectVo> getInfo(@NotNull(message = "主键不能为空")
                                      @PathVariable Long id) {
        return R.ok(iKxUserCollectService.queryById(id));
    }

    /**
     * 新增客户收藏
     */
    @RepeatSubmit()
    @PostMapping("/addCollect")
    public R<Void> addCollect(@RequestBody KxUserCollectBo bo) {
        Long userId = getAppLoginUser().getUserId();
        bo.setUserId(userId);
        return toAjax(iKxUserCollectService.insertByBo(bo));
    }

    /**
     * 删除客户收藏
     */
    @PostMapping("/deleteCollect")
    public R<Void> deleteCollect(@RequestBody KxUserCollectBo bo) {
        Long userId = getAppLoginUser().getUserId();
        bo.setUserId(userId);
        return toAjax(iKxUserCollectService.deleteCollect(bo));
    }

    /**
     * 删除客户收藏
     *
     * @param ids 主键串
     */
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iKxUserCollectService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
