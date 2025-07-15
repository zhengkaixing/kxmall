package com.kxmall.web.controller.dish.service;

import com.kxmall.common.core.domain.PageQuery;
import com.kxmall.common.core.page.TableDataInfo;
import com.kxmall.common.exception.ServiceException;
import com.kxmall.dish.domain.bo.CompositionDishDTO;
import com.kxmall.dish.domain.bo.DishSpuDTO;

public interface CompositionDishService {

    TableDataInfo<CompositionDishDTO> list(Long storageId, PageQuery pageQuery) throws ServiceException;

    DishSpuDTO queryById(Long id, Long userId) throws ServiceException;

    String addSale(Long id) throws ServiceException;
}