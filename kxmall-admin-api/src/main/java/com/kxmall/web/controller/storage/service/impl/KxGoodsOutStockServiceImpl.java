package com.kxmall.web.controller.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.enums.GoodsInStockType;
import com.kxmall.common.enums.StorageStatusType;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.common.helper.LoginHelper;
import com.kxmall.common.utils.StringUtils;
import com.kxmall.storage.domain.KxGoodsOutStock;
import com.kxmall.storage.domain.KxOutStockProduct;
import com.kxmall.storage.domain.KxStorage;
import com.kxmall.storage.domain.bo.KxGoodsOutStockBo;
import com.kxmall.storage.domain.vo.KxGoodsOutStockVo;
import com.kxmall.storage.domain.vo.KxOutStockProductVo;
import com.kxmall.storage.domain.vo.KxStorageVo;
import com.kxmall.storage.mapper.KxGoodsOutStockMapper;
import com.kxmall.storage.mapper.KxOutStockProductMapper;
import com.kxmall.storage.mapper.KxStockMapper;
import com.kxmall.storage.mapper.KxStorageMapper;
import com.kxmall.web.controller.storage.service.IKxGoodsOutStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品出库Service业务层处理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@RequiredArgsConstructor
@Service
public class KxGoodsOutStockServiceImpl implements IKxGoodsOutStockService {

    private final KxGoodsOutStockMapper baseMapper;
    private final KxStorageMapper storageMapper;
    private final KxOutStockProductMapper outStockProductMapper;
    private final KxStockMapper stockMapper;

    /**
     * 查询商品出库
     */
    @Override
    public KxGoodsOutStockVo queryById(Long id) {
        KxGoodsOutStockVo kxGoodsOutStockVo = baseMapper.selectVoById(id);
        List<KxOutStockProductVo> outStockProductVos = outStockProductMapper.selectVoList(new LambdaQueryWrapper<KxOutStockProduct>().eq(KxOutStockProduct::getOutStockNumbers, kxGoodsOutStockVo.getOutStockNumbers()));
        kxGoodsOutStockVo.setOutStockProductVoList(outStockProductVos);
        return kxGoodsOutStockVo;
    }

    /**
     * 查询商品出库列表
     */
    @Override
    public TableDataInfo<KxGoodsOutStockVo> queryPageList(KxGoodsOutStockBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxGoodsOutStock> lqw = buildQueryWrapper(bo);
        Page<KxGoodsOutStockVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<KxStorage> storages = storageMapper.selectList();
        result.getRecords().stream()
            .filter(record -> storages.stream().anyMatch(storage -> storage.getId().equals(record.getStorageId())))
            .forEach(record -> record.setStorageName(storages.stream()
                .filter(storage -> storage.getId().equals(record.getStorageId()))
                .findFirst()
                .map(KxStorage::getName)
                .orElse(null)));
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品出库列表
     */
    @Override
    public List<KxGoodsOutStockVo> queryList(KxGoodsOutStockBo bo) {
        LambdaQueryWrapper<KxGoodsOutStock> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxGoodsOutStock> buildQueryWrapper(KxGoodsOutStockBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxGoodsOutStock> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getStorageId() != null, KxGoodsOutStock::getStorageId, bo.getStorageId());
        lqw.eq(StringUtils.isNotBlank(bo.getOutStockNumbers()), KxGoodsOutStock::getOutStockNumbers, bo.getOutStockNumbers());
        lqw.eq(bo.getStates() != null, KxGoodsOutStock::getStates, bo.getStates());
        lqw.eq(StringUtils.isNotBlank(bo.getOutgoingPerson()), KxGoodsOutStock::getOutgoingPerson, bo.getOutgoingPerson());
        lqw.eq(bo.getOutgoingTime() != null, KxGoodsOutStock::getOutgoingTime, bo.getOutgoingTime());
        lqw.eq(StringUtils.isNotBlank(bo.getRemarks()), KxGoodsOutStock::getRemarks, bo.getRemarks());
        lqw.eq(StringUtils.isNotBlank(bo.getOutgoingDay()), KxGoodsOutStock::getOutgoingDay, bo.getOutgoingDay());
        return lqw;
    }

    /**
     * 新增商品出库
     */
    @Override
    public Boolean insertByBo(KxGoodsOutStockBo bo) {
        //自动生成入库单号,O+年月日+流水号
        //查询数据库最新生成的编号
        KxGoodsOutStock selectByMax = baseMapper.selectByMax();
        String max_code = "";//定义数据库的截取的数据
        String out_skock = "";//定义拼接好的字符串
        if (selectByMax != null) {
            max_code = selectByMax.getOutStockNumbers();
        }
        //定义时间字符串拼接
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String uid_pfix = simpleDateFormat.format(new Date());
        //判断数据库是否有数据
        if (max_code != null && max_code.contains(uid_pfix)) {
            String uid_end = max_code.substring(9, 14);
            Integer endNum = Integer.parseInt(uid_end);
            //100001
            endNum = 100000 + endNum + 1;
            String num = endNum + "";
            //去掉100001中的首位1
            String numm = num.substring(1);
            out_skock = "I" + uid_pfix + numm;
        } else {
            //数据库没数据时
            out_skock = "I" + uid_pfix + "00001";
        }
        //入库商品加入数据库
        List<KxOutStockProductVo> productVoList = bo.getOutStockProductVoList();
        if (!CollectionUtils.isEmpty(productVoList)) {
            for (KxOutStockProductVo stockProductVo : productVoList) {
                stockProductVo.setOutStockNumbers(out_skock);
                KxOutStockProduct stockProduct = BeanUtil.toBean(stockProductVo, KxOutStockProduct.class);
                if (outStockProductMapper.insert(stockProduct) <= 0) {
                    throw new ServiceException("入库商品添加失败");
                }
            }
        }
        //入库添加
        KxGoodsOutStock goodsInStock = BeanUtil.toBean(bo, KxGoodsOutStock.class);
        goodsInStock.setOutStockNumbers(out_skock);
        goodsInStock.setStates(GoodsInStockType.TO_BE_FOR_STOCK.getCode());
        goodsInStock.setUpdateTime(new Date());
        if (baseMapper.insert(goodsInStock) <= 0) {
            throw new ServiceException("管理员系统未知异常");
        }
        return true;

    }

    /**
     * 修改商品出库
     */
    @Override
    public Boolean updateByBo(KxGoodsOutStockBo bo) {
        LambdaQueryWrapper<KxOutStockProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(KxOutStockProduct::getOutStockNumbers, bo.getOutStockNumbers());
        if (outStockProductMapper.delete(wrapper) <= 0) {
            throw new ServiceException("出库商品更新失败");
        }
        //出库商品加入数据库
        List<KxOutStockProductVo> stockProductVoList = bo.getOutStockProductVoList();
        if (!CollectionUtils.isEmpty(stockProductVoList)) {
            for (KxOutStockProductVo goodsOutStockVo : stockProductVoList) {
                KxOutStockProduct stockProduct = BeanUtil.toBean(goodsOutStockVo, KxOutStockProduct.class);
                if (outStockProductMapper.insert(stockProduct) <= 0) {
                    throw new ServiceException("出库商品添加失败");
                }
            }
        }
        KxGoodsOutStock goodsOutStock = BeanUtil.toBean(bo, KxGoodsOutStock.class);
        goodsOutStock.setUpdateTime(new Date());
        if (baseMapper.updateById(goodsOutStock) > 0) {
            return true;
        }
        throw new ServiceException("管理员系统未知异常");

    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxGoodsOutStock entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品出库
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        List<KxGoodsOutStockVo> goodsOutStockVos = baseMapper.selectVoBatchIds(ids);
        //删除入库信息
        if (baseMapper.deleteBatchIds(ids) <= 0) {
            throw new ServiceException("出库商品删除失败");
        }
        for (KxGoodsOutStockVo outStockVo : goodsOutStockVos) {
            //批量删除入库商品
            LambdaQueryWrapper<KxOutStockProduct> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(KxOutStockProduct::getOutStockNumbers, outStockVo.getOutStockNumbers());
            if (outStockProductMapper.delete(wrapper) <= 0) {
                throw new ServiceException("出库商品删除失败");
            }
        }
        return true;
    }

    @Override
    public Boolean updateOutOfStock(KxGoodsOutStockBo bo) {
        if (ObjectUtils.isEmpty(bo.getStorageId()) && StringUtils.isEmpty(bo.getOutStockNumbers())) {
            throw new ServiceException("参数不能为空");
        }
        //根据入库的商品数量更新仓库的数量
        LambdaQueryWrapper<KxOutStockProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KxOutStockProduct::getOutStockNumbers, bo.getOutStockProductVoList());
        List<KxOutStockProduct> outStockProducts = outStockProductMapper.selectList(wrapper);
        Long inStockNum;//入库数量
        for (KxOutStockProduct outStockProduct : outStockProducts) {
            inStockNum = outStockProduct.getOutStockNum();
            Long productAttrId = outStockProduct.getProductAttrId();
            if (stockMapper.updateSock(bo.getStorageId(), productAttrId, inStockNum) <= 0) {
                throw new ServiceException("更新商品库存失败");
            }
        }
        //更新入库状态
        KxGoodsOutStock kxGoodsOutStock = new KxGoodsOutStock();
        kxGoodsOutStock.setStates(GoodsInStockType.IN_FOR_STOCK.getCode());
        kxGoodsOutStock.setOutgoingPerson(LoginHelper.getUsername());
        kxGoodsOutStock.setOutgoingTime(new Date());
        kxGoodsOutStock.setUpdateTime(new Date());
        LambdaQueryWrapper<KxGoodsOutStock> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(KxGoodsOutStock::getOutStockNumbers, bo.getOutStockNumbers());
        if (baseMapper.update(kxGoodsOutStock, wrapper1) <= 0) {
            throw new ServiceException("商品入库失败");
        }
        return true;
    }

    @Override
    public List<KxStorageVo> storagAllName(KxGoodsOutStockBo bo) {
        int state = StorageStatusType.NOMRAL.getCode();
        return storageMapper.getStorageNameAll(state, bo.getStorageIds());
    }
}
