package com.kxmall.product.domain.bo;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.kxmall.common.core.domain.BaseEntity;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 商品规格业务对象 kx_store_product_rule
 *
 * @author kxmall
 * @date 2023-02-08
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxStoreProductRuleBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 规格名称
     */
    @NotBlank(message = "规格名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ruleName;


    /**
     * 规格值
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONArray ruleValue;

    /**
     *
     */
    private Integer isDel;


}
