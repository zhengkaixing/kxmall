package com.kxmall.web.controller.user;

import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.user.domain.bo.KxUserBillBo;
import com.kxmall.user.domain.vo.KxUserBillVo;
import com.kxmall.web.controller.user.service.IKxAppUserBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户账单
 *
 * @author kxmall
 * @date 2024-02-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/userBill")
public class KxAppUserBillController extends BaseController {

    private final IKxAppUserBillService kxAppUserBillService;

    /**
     * 查询用户账单列表
     */
    @GetMapping("/list")
    public TableDataInfo<KxUserBillVo> list(KxUserBillBo bo, PageQuery pageQuery) {
        bo.setCategory("integral");
        return kxAppUserBillService.queryPageList(bo, pageQuery);
    }
}
