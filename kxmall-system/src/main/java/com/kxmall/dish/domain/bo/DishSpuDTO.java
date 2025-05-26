package com.kxmall.dish.domain.bo;

import com.kxmall.dish.domain.CompositionDishDO;
import com.kxmall.dish.domain.CompositionDishItemDO;
import lombok.Data;

import java.util.List;

@Data
public class DishSpuDTO extends CompositionDishDO {
    //用料管理
    private List<CompositionDishItemDO> dishSpuDOS;
}
