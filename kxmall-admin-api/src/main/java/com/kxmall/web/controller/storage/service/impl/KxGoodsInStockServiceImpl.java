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
import com.kxmall.storage.domain.KxGoodsInStock;
import com.kxmall.storage.domain.KxInStockProduct;
import com.kxmall.storage.domain.KxStorage;
import com.kxmall.storage.domain.bo.KxGoodsInStockBo;
import com.kxmall.storage.domain.vo.KxGoodsInStockVo;
import com.kxmall.storage.domain.vo.KxInStockProductVo;
import com.kxmall.storage.domain.vo.KxStorageVo;
import com.kxmall.storage.mapper.KxGoodsInStockMapper;
import com.kxmall.storage.mapper.KxInStockProductMapper;
import com.kxmall.storage.mapper.KxStockMapper;
import com.kxmall.storage.mapper.KxStorageMapper;
import com.kxmall.web.controller.storage.service.IKxGoodsInStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品入库Service业务层处理
 *
 * @author kxmall
 * @date 2023-08-27
 */
@RequiredArgsConstructor
@Service
public class KxGoodsInStockServiceImpl implements IKxGoodsInStockService {

    private final KxGoodsInStockMapper baseMapper;
    private final KxInStockProductMapper inStockProductMapper;
    private final KxStorageMapper storageMapper;
    private final KxStockMapper stockMapper;

    /**
     * 查询商品入库
     */
    @Override
    public KxGoodsInStockVo queryById(Long id) {
        KxGoodsInStockVo kxGoodsInStockVo = baseMapper.selectVoById(id);
        List<KxInStockProductVo> inStockProductVoList = inStockProductMapper.selectVoList(new LambdaQueryWrapper<KxInStockProduct>().eq(KxInStockProduct::getInStockNumbers, kxGoodsInStockVo.getInStockNumbers()));
        kxGoodsInStockVo.setInStockProductVoList(inStockProductVoList);
        return kxGoodsInStockVo;
    }

    /**
     * 查询商品入库列表
     */
    @Override
    public TableDataInfo<KxGoodsInStockVo> queryPageList(KxGoodsInStockBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KxGoodsInStock> lqw = buildQueryWrapper(bo);
        Page<KxGoodsInStockVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
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
     * 查询商品入库列表
     */
    @Override
    public List<KxGoodsInStockVo> queryList(KxGoodsInStockBo bo) {
        LambdaQueryWrapper<KxGoodsInStock> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<KxGoodsInStock> buildQueryWrapper(KxGoodsInStockBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<KxGoodsInStock> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getStorageId() != null, KxGoodsInStock::getStorageId, bo.getStorageId());
        lqw.eq(StringUtils.isNotBlank(bo.getInStockNumbers()), KxGoodsInStock::getInStockNumbers, bo.getInStockNumbers());
        lqw.eq(bo.getStates() != null, KxGoodsInStock::getStates, bo.getStates());
        lqw.eq(StringUtils.isNotBlank(bo.getIngoingPerson()), KxGoodsInStock::getIngoingPerson, bo.getIngoingPerson());
        lqw.eq(bo.getIngoingTime() != null, KxGoodsInStock::getIngoingTime, bo.getIngoingTime());
        lqw.eq(StringUtils.isNotBlank(bo.getRemarks()), KxGoodsInStock::getRemarks, bo.getRemarks());
        lqw.eq(StringUtils.isNotBlank(bo.getOutgoingDay()), KxGoodsInStock::getOutgoingDay, bo.getOutgoingDay());
        return lqw;
    }

    /**
     * 新增商品入库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(KxGoodsInStockBo bo) {
        //自动生成入库单号,O+年月日+流水号
        //查询数据库最新生成的编号
        KxGoodsInStock selectByMax = baseMapper.selectByMax();
        String max_code = "";//定义数据库的截取的数据
        String in_skock = "";//定义拼接好的字符串
        if (selectByMax != null) {
            max_code = selectByMax.getInStockNumbers();
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
            in_skock = "I" + uid_pfix + numm;
        } else {
            //数据库没数据时
            in_skock = "I" + uid_pfix + "00001";
        }
        //入库商品加入数据库
        List<KxInStockProductVo> productVoList = bo.getInStockProductVoList();
        if (!CollectionUtils.isEmpty(productVoList)) {
            for (KxInStockProductVo stockProductVo : productVoList) {
                stockProductVo.setInStockNumbers(in_skock);
                KxInStockProduct stockProduct = BeanUtil.toBean(stockProductVo, KxInStockProduct.class);
                if (inStockProductMapper.insert(stockProduct) <= 0) {
                    throw new ServiceException("入库商品添加失败");
                }
            }
        }
        //入库添加
        KxGoodsInStock goodsInStock = BeanUtil.toBean(bo, KxGoodsInStock.class);
        goodsInStock.setInStockNumbers(in_skock);
        goodsInStock.setStates(GoodsInStockType.TO_BE_FOR_STOCK.getCode());
        goodsInStock.setUpdateTime(new Date());
        if (baseMapper.insert(goodsInStock) <= 0) {
            throw new ServiceException("管理员系统未知异常");
        }
        return true;
    }

    /**
     * 修改商品入库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(KxGoodsInStockBo bo) {
        LambdaQueryWrapper<KxInStockProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(KxInStockProduct::getInStockNumbers, bo.getInStockNumbers());
        if (inStockProductMapper.delete(wrapper) <= 0) {
            throw new ServiceException("入库商品更新失败");
        }
        //入库商品加入数据库
        List<KxInStockProductVo> stockProductVoList = bo.getInStockProductVoList();
        if (!CollectionUtils.isEmpty(stockProductVoList)) {
            for (KxInStockProductVo inStockProductVo : stockProductVoList) {
                KxInStockProduct stockProduct = BeanUtil.toBean(inStockProductVo, KxInStockProduct.class);
                if (inStockProductMapper.insert(stockProduct) <= 0) {
                    throw new ServiceException("入库商品添加失败");
                }
            }
        }
        KxGoodsInStock goodsInStock = BeanUtil.toBean(bo, KxGoodsInStock.class);
        goodsInStock.setUpdateTime(new Date());
        if (baseMapper.updateById(goodsInStock) > 0) {
            return true;
        }
        throw new ServiceException("管理员系统未知异常");

    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(KxGoodsInStock entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品入库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        List<KxGoodsInStockVo> goodsInStockVos = baseMapper.selectVoBatchIds(ids);
        //删除入库信息
        if (baseMapper.deleteBatchIds(ids) <= 0) {
            throw new ServiceException("入库商品删除失败");
        }
        for (KxGoodsInStockVo inStockVo : goodsInStockVos) {
            //批量删除入库商品
            LambdaQueryWrapper<KxInStockProduct> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(KxInStockProduct::getInStockNumbers, inStockVo.getInStockNumbers());
            if (inStockProductMapper.delete(wrapper) <= 0) {
                throw new ServiceException("入库商品删除失败");
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateInOfStock(KxGoodsInStockBo bo) {
        if (ObjectUtils.isEmpty(bo.getStorageId()) && StringUtils.isEmpty(bo.getInStockNumbers())) {
            throw new ServiceException("参数不能为空");
        }
        //根据入库的商品数量更新仓库的数量
        LambdaQueryWrapper<KxInStockProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KxInStockProduct::getInStockNumbers, bo.getInStockNumbers());
        List<KxInStockProduct> inStockProducts = inStockProductMapper.selectList(wrapper);
        Long inStockNum;//入库数量
        for (KxInStockProduct inStockProduct : inStockProducts) {
            inStockNum = inStockProduct.getInStockNum();
            Long productAttrId = inStockProduct.getProductAttrId();
            if (stockMapper.updateSockForAdd(bo.getStorageId(), productAttrId, inStockNum) <= 0) {
                throw new ServiceException("更新商品库存失败");
            }
        }
        //更新入库状态
        KxGoodsInStock kxGoodsInStock = new KxGoodsInStock();
        kxGoodsInStock.setStates(GoodsInStockType.IN_FOR_STOCK.getCode());
        kxGoodsInStock.setIngoingPerson(LoginHelper.getUsername());
        kxGoodsInStock.setIngoingTime(new Date());
        kxGoodsInStock.setUpdateTime(new Date());
        LambdaQueryWrapper<KxGoodsInStock> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(KxGoodsInStock::getInStockNumbers, bo.getInStockNumbers());
        if (baseMapper.update(kxGoodsInStock, wrapper1) <= 0) {
            throw new ServiceException("商品入库失败");
        }
        return true;
    }

    /**
     * 获取所有仓库的名称
     *
     * @param bo
     * @return
     */
    @Override
    public List<KxStorageVo> storagAllName(KxGoodsInStockBo bo) {
        int state = StorageStatusType.NOMRAL.getCode();
        return storageMapper.getStorageNameAll(state, bo.getStorageIds());
    }
}
