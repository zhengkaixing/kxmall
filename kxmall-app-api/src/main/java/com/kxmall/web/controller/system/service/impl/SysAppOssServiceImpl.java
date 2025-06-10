package com.kxmall.web.controller.system.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kxmall.common.constant.FileConstants;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.BeanCopyUtils;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.common.utils.io.FilesUtils;
import com.kxmall.oss.core.OssClient;
import com.kxmall.oss.domain.SysOssContent;
import com.kxmall.oss.entity.UploadResult;
import com.kxmall.oss.enumd.AccessPolicyType;
import com.kxmall.oss.factory.OssFactory;
import com.kxmall.oss.mapper.SysOssContentMapper;
import com.kxmall.oss.properties.OssProperties;
import com.kxmall.system.domain.SysOss;
import com.kxmall.system.domain.SysOssConfig;
import com.kxmall.system.domain.bo.SysOssBo;
import com.kxmall.system.domain.vo.SysOssConfigVo;
import com.kxmall.system.domain.vo.SysOssVo;
import com.kxmall.system.mapper.SysOssConfigMapper;
import com.kxmall.system.mapper.SysOssMapper;
import com.kxmall.web.controller.system.service.ISysAppOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 文件上传 服务层实现
 *
 * @author 郅兴开源团队-小黑
 */
@RequiredArgsConstructor
@Service
public class SysAppOssServiceImpl implements ISysAppOssService {

    private final SysOssMapper baseMapper;

    private final SysOssConfigMapper sysOssConfigMapper;

    private final SysOssContentMapper ossContentMapper;


    private LambdaQueryWrapper<SysOss> buildQueryWrapper(SysOssBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOss> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getFileName()), SysOss::getFileName, bo.getFileName());
        lqw.like(StringUtils.isNotBlank(bo.getOriginalName()), SysOss::getOriginalName, bo.getOriginalName());
        lqw.eq(StringUtils.isNotBlank(bo.getFileSuffix()), SysOss::getFileSuffix, bo.getFileSuffix());
        lqw.eq(StringUtils.isNotBlank(bo.getUrl()), SysOss::getUrl, bo.getUrl());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
                SysOss::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(StringUtils.isNotBlank(bo.getCreateBy()), SysOss::getCreateBy, bo.getCreateBy());
        lqw.eq(StringUtils.isNotBlank(bo.getService()), SysOss::getService, bo.getService());
        return lqw;
    }


    @Override
    public SysOssVo upload(MultipartFile file) {
        //获取当前启动的配置
        SysOssConfigVo configVo = sysOssConfigMapper.selectVoOne(new LambdaQueryWrapper<SysOssConfig>().eq(SysOssConfig::getStatus, 0));
        if (FileConstants.DISK.equals(configVo.getConfigKey())) {
            return disk(file);
        }else if(FileConstants.DATABASE.equals(configVo.getConfigKey())){
            return database(file);
        }else {
            return getSysOssVo(file);
        }
    }

    private SysOssVo getSysOssVo(MultipartFile file) {
        String originalfileName = file.getOriginalFilename();
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult;
        try {
            uploadResult = storage.uploadSuffix(file.getBytes(), suffix, file.getContentType());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
        // 保存文件信息
        SysOss oss = new SysOss();
        oss.setUrl(uploadResult.getUrl());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.getFilename());
        oss.setOriginalName(originalfileName);
        oss.setService(storage.getConfigKey());
        baseMapper.insert(oss);
        SysOssVo sysOssVo = new SysOssVo();
        BeanCopyUtils.copy(oss, sysOssVo);
        return this.matchingUrl(sysOssVo);
    }

    /**
     * 匹配Url
     *
     * @param oss OSS对象
     * @return oss 匹配Url的OSS对象
     */
    private SysOssVo matchingUrl(SysOssVo oss) {
        OssClient storage = OssFactory.instance(oss.getService());
        // 仅修改桶类型为 private 的URL，临时URL时长为120s
        if (AccessPolicyType.PRIVATE == storage.getAccessPolicy()) {
            oss.setUrl(storage.getPrivateUrl(oss.getFileName(), 120));
        }
        return oss;
    }

    public SysOssVo disk(MultipartFile file) {

        try {
            String name = file.getOriginalFilename();
            String suffix = StringUtils.substring(name, name.lastIndexOf("."), name.length());
            byte[] content = IoUtil.readBytes(file.getInputStream());
            String path = FilesUtils.generatePath(content, name);
            // 执行写入
            String filePath = getFilePath(path);
            FileUtil.writeBytes(content, filePath);
            OssProperties config = OssFactory.instanceConfig();
            // 拼接返回路径
            String fileUrl = this.formatFileUrl(config.getEndpoint(),FileConstants.DISK, path);

            // 保存文件信息
            SysOss oss = new SysOss();
            oss.setUrl(fileUrl);
            oss.setFileSuffix(suffix);
            oss.setFileName(name);
            oss.setOriginalName(name);
            oss.setService(FileConstants.DISK);
            baseMapper.insert(oss);
            SysOssVo sysOssVo = new SysOssVo();
            BeanCopyUtils.copy(oss, sysOssVo);
            return sysOssVo;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public SysOssVo database(MultipartFile file) {

        try {
            String name = file.getOriginalFilename();
            String suffix = StringUtils.substring(name, name.lastIndexOf("."), name.length());
            byte[] content = IoUtil.readBytes(file.getInputStream());
            String path = FilesUtils.generatePath(content, name);

            OssProperties config = OssFactory.instanceConfig();

            SysOssContent ossContent = new SysOssContent();
            ossContent.setPath(path);
            ossContent.setConfigKey(FileConstants.DATABASE);
            ossContent.setContent(content);
            ossContentMapper.insert(ossContent);
            // 拼接返回路径
            String fileUrl = this.formatFileUrl(config.getEndpoint(), FileConstants.DATABASE, path);

            // 保存文件信息
            SysOss oss = new SysOss();
            oss.setUrl(fileUrl);
            oss.setFileSuffix(suffix);
            oss.setFileName(name);
            oss.setOriginalName(name);
            oss.setService(FileConstants.DATABASE);
            baseMapper.insert(oss);
            SysOssVo sysOssVo = new SysOssVo();
            BeanCopyUtils.copy(oss, sysOssVo);
            return sysOssVo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFilePath(String path) {
        OssProperties ossProperties = OssFactory.instanceConfig();
        return ossProperties.getDomain() +"/" + path;
    }


    /**
     * 格式化文件的 URL 访问地址
     * 使用场景：local、ftp、db，通过 FileController 的 getFile 来获取文件内容
     *
     * @param domain 自定义域名
     * @param path 文件路径
     * @return URL 访问地址
     */
    protected String formatFileUrl(String domain, String type,String path) {
        return StrUtil.format("{}/system/oss/{}/get/{}", domain, type, path);
    }
}
