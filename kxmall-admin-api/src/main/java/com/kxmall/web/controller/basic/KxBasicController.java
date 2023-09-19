package com.kxmall.web.controller.basic;

import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基础信息
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/basic")
public class KxBasicController extends BaseController {


    private final static String MAP_KEY = "mapKey";

    @Value("${com.kxmall.address.map.key}")
    private String geocodeKey;

    /**
     * 查询基础信息
     */
    @GetMapping("/queryBasic")
    public R<String> queryBasic(String key) {
        if (MAP_KEY.equals(key)) {
            return R.ok(geocodeKey);
        }
        return R.fail("不存在该key的值");
    }


}
