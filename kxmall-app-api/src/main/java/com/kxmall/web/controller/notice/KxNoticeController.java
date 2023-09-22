package com.kxmall.web.controller.notice;

import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.R;
import com.kxmall.system.domain.SysNotice;
import com.kxmall.web.controller.notice.service.IKxNoticetService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公告
 *
 * @author kxmall
 * @date 2023-04-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/notice/app")
public class KxNoticeController extends BaseAppController {

    private final IKxNoticetService noticetService;

    /**
     * 查询客户收藏列表
     */
    @GetMapping("/list")
    public R<List<SysNotice>> list(SysNotice bo) {
        return R.ok(noticetService.queryList(bo));
    }

}
