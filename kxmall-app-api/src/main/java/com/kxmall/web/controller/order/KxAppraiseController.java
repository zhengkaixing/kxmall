package com.kxmall.web.controller.order;

import com.kxmall.common.annotation.RepeatSubmit;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.order.domain.bo.AppraiseRequestBo;
import com.kxmall.order.domain.vo.KxStoreAppraiseVo;
import com.kxmall.web.controller.order.service.IKxAppAppraiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 评论
 *
 * @author kxmall
 * @date 2023-04-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/appraise/app")
public class KxAppraiseController extends BaseAppController {

    private final IKxAppAppraiseService kxAppraiseService;

    /**
     * 增加评论
     */
    @RepeatSubmit()
    @PostMapping("/addAppraise")
    public R<Void> addAppraise(@RequestBody AppraiseRequestBo bo) {
        Long userId = getAppLoginUser().getUserId();
        return toAjax(kxAppraiseService.addAppraise(bo, userId));
    }

    /**
     * 查询商品的所有评论
     *
     * @param productId
     * @return
     */
    @GetMapping("/getSpuAllAppraise")
    public TableDataInfo<KxStoreAppraiseVo> getSpuAllAppraise(Long productId, Integer pageNo, Integer pageSize) {
        return kxAppraiseService.getProductAppraiseByPage(productId, pageNo, pageSize, 1);
    }


}
