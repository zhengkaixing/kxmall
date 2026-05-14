package com.kxmall.web.controller.seckill;

import cn.dev33.satoken.annotation.SaIgnore;
import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.seckill.domain.vo.KxStoreSeckillVo;
import com.kxmall.web.controller.seckill.service.IKxAppStoreSeckillService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品秒杀
 *
 * @author kxmall
 * @date 2024-05-15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/seckill/app")
public class KxAppStoreSeckillController extends BaseController {

    private final IKxAppStoreSeckillService kxAppStoreSeckillService;

    /**
     * 查询当前时间秒杀内容
     */
    @SaIgnore
    @GetMapping("/listCurrent")
    public R<List<KxStoreSeckillVo>> listCurrent(String storageId, @RequestParam(defaultValue = "0") Integer sort) {
        if (StringUtils.isEmpty(storageId) || storageId.equals("null")) {
            return R.ok(null);
        }
        Long storageIdLong = Long.valueOf(storageId);
        return R.ok(kxAppStoreSeckillService.listCurrent(storageIdLong, sort));
    }

    /**
     * 获取商品秒杀详细信息
     *
     * @param id 主键
     */
    @SaIgnore
    @GetMapping("/{id}")
    public R<KxStoreSeckillVo> getInfo(@NotNull(message = "主键不能为空")
                                       @PathVariable Long id) {
        return R.ok(kxAppStoreSeckillService.queryById(id));
    }

}
