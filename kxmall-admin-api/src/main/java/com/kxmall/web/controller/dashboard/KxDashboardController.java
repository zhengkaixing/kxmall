package com.kxmall.web.controller.dashboard;

import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.R;
import com.kxmall.dashboard.domain.SalesStatementDTO;
import com.kxmall.dashboard.domain.UserStatementDTO;
import com.kxmall.web.controller.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 商铺广告
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/dashboard/dashboard")
public class KxDashboardController extends BaseController {

    private final DashboardService dashboardService;

    /**
     * 用户数量统计
     *
     * @param
     * @return
     */
    @GetMapping("/countUser")
    public R<List<UserStatementDTO>> countUser() {
        return R.ok(dashboardService.countUser());
    }

    /**
     * 销售统计
     *
     * @param storageId
     * @return
     */
    @GetMapping("/getSalesStatement")
    public R<List<SalesStatementDTO>> getSalesStatement(Long storageId) {
        Set<Long> storagePermission = getLoginUser().getStoragePermission();
        return R.ok(dashboardService.getSalesStatement(storageId,storagePermission));
    }

    /**
     * 近两日销售统计
     *
     * @param storageId
     * @return
     */
    @GetMapping("/getTodayAndYesterdaySales")
    public R<List<SalesStatementDTO>> getTodayAndYesterdaySales(Long storageId) {
        Set<Long> storagePermission = getLoginUser().getStoragePermission();
        return R.ok(dashboardService.getTodayAndYesterdaySales(storageId,storagePermission));
    }

    /**
     * 按小时统计销量
     *
     * @param storageId
     * @return
     */
    @GetMapping("/getSalesByHour")
    public R<List<SalesStatementDTO>> getSalesByHour(Long storageId) {
        Set<Long> storagePermission = getLoginUser().getStoragePermission();
        return R.ok(dashboardService.getSalesByHour(storageId,storagePermission));
    }
}
