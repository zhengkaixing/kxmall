package com.kxmall.web.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.core.domain.entity.SysDictData;
import com.kxmall.system.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author kxmall
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/app/dict/data")
public class SysAppDictDataController extends BaseController {


    private final ISysDictTypeService dictTypeService;




    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型
     */
    @GetMapping(value = "/type/{dictType}")
    public R<List<SysDictData>> dictType(@PathVariable String dictType) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (ObjectUtil.isNull(data)) {
            data = new ArrayList<>();
        }
        return R.ok(data);
    }


}
