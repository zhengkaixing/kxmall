package com.kxmall.web.controller.carousel;

import cn.dev33.satoken.annotation.SaIgnore;
import com.kxmall.carousel.domain.KxCarousel;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.domain.R;
import com.kxmall.web.controller.carousel.service.IKxAppCarouselService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * app推广
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/carousel/app")
public class KxAppCarouselController extends BaseAppController {

    private final IKxAppCarouselService appCarouselService;

    /**
     * 取得活跃广告
     *
     * @param adType
     */
    @SaIgnore
    @GetMapping("/getCarouselActive")
    public R<List<KxCarousel>> getCarouselActive(@NotNull(message = "type不能为空") Integer adType) {
        return R.ok(appCarouselService.listAll(adType));
    }
}
