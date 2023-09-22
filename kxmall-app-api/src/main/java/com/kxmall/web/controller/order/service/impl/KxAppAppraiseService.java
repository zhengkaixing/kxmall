package com.kxmall.web.controller.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.utils.redis.RedisUtils;
import com.kxmall.order.domain.KxStoreAppraise;
import com.kxmall.order.domain.vo.KxStoreAppraiseVo;
import com.kxmall.order.mapper.KxStoreAppraiseMapper;
import com.kxmall.storage.domain.vo.KxStockVo;
import com.kxmall.web.controller.order.service.IKxAppAppraiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kaixin
 * @version 1.0
 * @date 2023/9/6
 */

@RequiredArgsConstructor
@Service
public class KxAppAppraiseService implements IKxAppAppraiseService {


    private final KxStoreAppraiseMapper baseMapper;

    public static final String CA_APPRAISE_KEY = "CA_APPRAISE_";

    @Override
    public TableDataInfo<KxStoreAppraiseVo> getProductAppraiseByPage(Long productId, Integer pageNo, Integer pageSize, Integer state) {
        String cacheKey = CA_APPRAISE_KEY + productId + "_" + pageNo + "_" + pageSize;
        TableDataInfo<KxStoreAppraiseVo> obj = RedisUtils.getCacheObject(cacheKey);
        if (obj != null) {
            return obj;
        }
        Long count = baseMapper.selectCount(new QueryWrapper<KxStoreAppraise>().eq("product_id", productId).eq("state", state));
        Integer offset = pageSize * (pageNo - 1);
        List<KxStoreAppraiseVo> storeAppraiseVoList = baseMapper.selectProductAppraiseByPage(productId, offset, pageSize);
//        for (KxStoreAppraiseVo appraiseResponseDTO : storeAppraiseVoList) {
//            appraiseResponseDTO.setImgList(imgMapper.getImgs(BizType.COMMENT.getCode(), appraiseResponseDTO.getId()));
//        }

        TableDataInfo<KxStoreAppraiseVo> info = new TableDataInfo<>(storeAppraiseVoList, count);
        RedisUtils.setCacheObject(cacheKey, info);
        return info;
    }
}
