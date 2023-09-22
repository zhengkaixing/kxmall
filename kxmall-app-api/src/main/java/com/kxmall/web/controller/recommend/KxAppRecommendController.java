package com.kxmall.web.controller.recommend;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.ObjectUtil;
import com.kxmall.common.core.controller.BaseAppController;
import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.domain.entity.SysDictData;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.recommend.domain.vo.KxRecommendVo;
import com.kxmall.web.controller.recommend.service.IKxAppRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * app推荐类
 *
 * @author kxmall
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
    public R<List<SysDictData>> dictType() {
        List<SysDictData> data = recommendService.getRecommendTypeEnums();
        if (ObjectUtil.isNull(data)) {
            data = new ArrayList<>();
        }
        return R.ok(data);
    }


}
