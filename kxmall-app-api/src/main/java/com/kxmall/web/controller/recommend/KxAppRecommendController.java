package com.kxmall.web.controller.recommend;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.ObjectUtil;
import com.kxmall.carousel.domain.KxCarousel;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.R;
import com.kxmall.web.controller.recommend.service.IKxAppRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * app推荐类
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/recommend/app")
public class KxAppRecommendController extends BaseAppController {

    private final IKxAppRecommendService recommendService;


    /**
     * 获取推荐枚举
     */
    @SaIgnore
    @GetMapping("/getRecommendTypeEnums")
    public R<List<KxCarousel>> dictType() {
        List<KxCarousel> data = recommendService.getRecommendTypeEnums();
        if (ObjectUtil.isNull(data)) {
            data = new ArrayList<>();
        }
        return R.ok(data);
    }


}
