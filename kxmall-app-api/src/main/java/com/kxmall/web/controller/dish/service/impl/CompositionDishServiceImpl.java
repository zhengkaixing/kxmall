package com.kxmall.web.controller.dish.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.dish.domain.CompositionDishDO;
import com.kxmall.dish.domain.CompositionDishItemDO;
import com.kxmall.dish.domain.bo.CompositionDishDTO;
import com.kxmall.dish.domain.bo.DishSpuDTO;
import com.kxmall.dish.mapper.CompositionDishItemMapper;
import com.kxmall.dish.mapper.CompositionDishMapper;
import com.kxmall.order.domain.KxStoreCart;
import com.kxmall.order.mapper.KxStoreCartMapper;
import com.kxmall.storage.domain.KxStorage;
import com.kxmall.storage.mapper.KxStorageMapper;
import com.kxmall.web.controller.dish.service.CompositionDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompositionDishServiceImpl implements CompositionDishService {

    private final CompositionDishMapper compositionDishMapper;
    private final CompositionDishItemMapper compositionDishItemMapper;
    private final KxStorageMapper storageMapper;
    private final KxStoreCartMapper cartMapper;

    @Override
    public TableDataInfo<CompositionDishDTO> list(Long storageId, PageQuery pageQuery) throws ServiceException {
        LambdaQueryWrapper<CompositionDishDO> wrapper = new LambdaQueryWrapper<>();
        if (storageId != null) {
            wrapper.eq(CompositionDishDO::getStorageId, storageId);
        }
        wrapper.orderByDesc(CompositionDishDO::getId);

        IPage<CompositionDishDTO> page = compositionDishMapper.selectVoPage(pageQuery.build(), wrapper);
        List<KxStorage> storageList = storageMapper.selectList(null);

        List<CompositionDishDTO> collect = page.getRecords().stream().map(compositionDishDO -> {
            CompositionDishDTO dto = new CompositionDishDTO();
            BeanUtils.copyProperties(compositionDishDO, dto);
            storageList.stream().filter(storage -> storage.getId().equals(compositionDishDO.getStorageId())).findFirst().ifPresent(storage -> dto.setStorageName(storage.getName()));
            return dto;
        }).collect(Collectors.toList());
        page.setRecords(collect);
        return TableDataInfo.build(page);
    }

    @Override
    public DishSpuDTO queryById(Long id, Long userId) throws ServiceException {
        DishSpuDTO dto = new DishSpuDTO();
        CompositionDishDO compositionDishDO = compositionDishMapper.selectById(id);
        BeanUtils.copyProperties(compositionDishDO, dto);

        LambdaQueryWrapper<CompositionDishItemDO> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(CompositionDishItemDO::getDishId, id);
        List<CompositionDishItemDO> itemList = compositionDishItemMapper.selectList(itemWrapper);

        LambdaQueryWrapper<KxStoreCart> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(KxStoreCart::getUid, userId);
        List<KxStoreCart> cartList = cartMapper.selectList(cartWrapper);

        itemList.forEach(item -> {
            item.setNum(0L);
            cartList.stream().filter(cart -> cart.getProductId().equals(item.getProductId())).findFirst().ifPresent(cart -> item.setNum(cart.getCartNum()));
        });

        dto.setDishSpuDOS(itemList);
        return dto;
    }

    @Override
    public String addSale(Long id) throws ServiceException {
        CompositionDishDO compositionDishDO = compositionDishMapper.selectById(id);
        if (compositionDishDO == null) {
            throw new ServiceException("菜品不存在");
        }
        compositionDishDO.setSales(compositionDishDO.getSales() + 1);
        compositionDishMapper.updateById(compositionDishDO);
        return "ok";
    }
}