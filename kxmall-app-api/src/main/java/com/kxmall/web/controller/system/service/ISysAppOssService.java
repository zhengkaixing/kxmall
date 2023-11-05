package com.kxmall.web.controller.system.service;

import com.kxmall.system.domain.vo.SysOssVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传 服务层
 *
 * @author kxmall
 */
public interface ISysAppOssService {


    SysOssVo upload(MultipartFile file);


}
