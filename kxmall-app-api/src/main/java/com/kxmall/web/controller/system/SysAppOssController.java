package com.kxmall.web.controller.system;


import cn.hutool.core.util.ObjectUtil;
import com.kxmall.common.core.controller.BaseController;
import com.kxmall.common.core.domain.R;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.system.domain.vo.SysOssVo;
import com.kxmall.web.controller.system.service.ISysAppOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传 控制层
 *
 * @author kxmall
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oss/app")
public class SysAppOssController extends BaseController {

    private final ISysAppOssService iSysOssService;


    /**
     * 上传OSS对象存储
     *
     * @param file 文件
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Map<String, String>> upload(@RequestPart("file") MultipartFile file) {
        if (ObjectUtil.isNull(file)) {
            throw new ServiceException("上传文件不能为空");
        }
        SysOssVo oss = iSysOssService.upload(file);
        Map<String, String> map = new HashMap<>(2);
        map.put("url", oss.getUrl());
        map.put("fileName", oss.getOriginalName());
        return R.ok(map);
    }


}
