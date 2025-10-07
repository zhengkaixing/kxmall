package com.kxmall.web.controller.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.constant.CacheNames;
import com.kxmall.common.constant.FileConstants;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.utils.BeanCopyUtils;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.common.utils.file.FileUtils;
import com.kxmall.common.utils.io.FilesUtils;
import com.kxmall.common.utils.spring.SpringUtils;
import com.kxmall.oss.core.OssClient;
import com.kxmall.oss.domain.SysOssContent;
import com.kxmall.oss.entity.UploadResult;
import com.kxmall.oss.enumd.AccessPolicyType;
import com.kxmall.oss.factory.OssFactory;
import com.kxmall.oss.mapper.SysOssContentMapper;
import com.kxmall.oss.properties.OssProperties;
import com.kxmall.system.domain.SysOss;
import com.kxmall.system.domain.bo.SysOssBo;
import com.kxmall.system.domain.vo.SysOssConfigVo;
import com.kxmall.system.domain.vo.SysOssVo;
import com.kxmall.system.mapper.SysOssMapper;
import com.kxmall.web.controller.system.service.ISysOssConfigService;
import com.kxmall.web.controller.system.service.ISysOssService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件上传 服务层实现
 *
 * @author 郅兴开源团队-小黑
 */
@RequiredArgsConstructor
@Service
public class SysOssServiceImpl implements ISysOssService {

    private final SysOssMapper baseMapper;

    private final ISysOssConfigService configService;

    private final SysOssContentMapper ossContentMapper;

    @Override
    public TableDataInfo<SysOssVo> queryPageList(SysOssBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOss> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(SysOss::getCreateTime);
        Page<SysOssVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<SysOssVo> filterResult = result.getRecords().stream()
                .map(record -> {
                    if (FileConstants.DISK.equals(record.getService())) {
                        return record; // 直接返回，不执行 matchingUrl
                    }
                    if (FileConstants.DATABASE.equals(record.getService())) {
                        return record; // 直接返回，不执行 matchingUrl
                    }
                    return matchingUrl(record); // 执行 matchingUrl
                })
                .collect(Collectors.toList());
        result.setRecords(filterResult);
        return TableDataInfo.build(result);
    }

    @Override
    public List<SysOssVo> listByIds(Collection<Long> ossIds) {
        List<SysOssVo> list = new ArrayList<>();
        for (Long id : ossIds) {
            SysOssVo vo = SpringUtils.getAopProxy(this).getById(id);
            if (ObjectUtil.isNotNull(vo)) {
                if(FileConstants.DISK.equals(vo.getService())){
                    list.add(vo);
                }else if (FileConstants.DATABASE.equals(vo.getService())){
                    list.add(vo);
                }else {
                    list.add(this.matchingUrl(vo));
                }
            }
        }
        return list;
    }

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

    @Cacheable(cacheNames = CacheNames.SYS_OSS, key = "#ossId")
    @Override
    public SysOssVo getById(Long ossId) {
        return baseMapper.selectVoById(ossId);
    }

    @Override
    public void download(Long ossId, HttpServletResponse response) throws IOException {
        SysOssVo sysOss = SpringUtils.getAopProxy(this).getById(ossId);
        if (ObjectUtil.isNull(sysOss)) {
            throw new ServiceException("文件数据不存在!");
        }
        FileUtils.setAttachmentResponseHeader(response, sysOss.getOriginalName());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");
        OssClient storage = OssFactory.instance();
        try(InputStream inputStream = storage.getObjectContent(sysOss.getUrl())) {
            int available = inputStream.available();
            IoUtil.copy(inputStream, response.getOutputStream(), available);
            response.setContentLength(available);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public SysOssVo upload(MultipartFile file) {

        //获取当前启动的配置
        SysOssConfigVo configVo = configService.selectDefault();
        if (ObjectUtils.isEmpty(configVo)) {
            throw new ServiceException("请至少选择一个文件上传配置！");
        }
        if (FileConstants.DISK.equals(configVo.getConfigKey())) {
            return disk(file);
        }else if(FileConstants.DATABASE.equals(configVo.getConfigKey())){
            return database(file);
        }else {
            return getSysOssVo(file);
        }
    }

    @NotNull
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

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        List<SysOss> list = baseMapper.selectBatchIds(ids);
        for (SysOss sysOss : list) {
            if (!FileConstants.DISK.equals(sysOss.getService()) && !FileConstants.DATABASE.equals(sysOss.getService())) {
                OssClient storage = OssFactory.instance(sysOss.getService());
                storage.delete(sysOss.getUrl());
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
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

    private String getFilePath(String path) {
        OssProperties ossProperties = OssFactory.instanceConfig();
        return ossProperties.getDomain() +"/" + path;
    }

    @Override
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

    @Override
    public byte[] getFileContent(String configKey, String path) {
        if (FileConstants.DISK.equals(configKey)) {
            String filePath = getFilePath(path);
            return FileUtil.readBytes(filePath);
        }else if (FileConstants.DATABASE.equals(configKey)) {
            List<SysOssContent> list = ossContentMapper.selectList(
                    buildQuery(configKey, path).select(SysOssContent::getContent).orderByDesc(SysOssContent::getId));
            return Optional.ofNullable(CollUtil.getFirst(list))
                    .map(SysOssContent::getContent)
                    .orElse(null);
        }
        return null;
    }

    private LambdaQueryWrapper<SysOssContent> buildQuery(String configKey, String path) {
        return new LambdaQueryWrapper<SysOssContent>()
                .eq(SysOssContent::getConfigKey, configKey)
                .eq(SysOssContent::getPath, path);
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
}
