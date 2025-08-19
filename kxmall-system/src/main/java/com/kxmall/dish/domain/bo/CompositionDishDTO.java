package com.kxmall.dish.domain.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 套餐/组合菜品 数据传输对象 (Data Transfer Object)
 * 用于在应用程序层或接口层传输套餐菜品的基本信息
 * Generate Code By kxmall
 * @author kxmall
 */
@Data // Lombok注解，自动生成getter, setter, toString, equals, hashCode等方法
public class CompositionDishDTO {

    /**
     * 主键ID (套餐菜品唯一标识)
     */
    private Long id;

    /**
     * 所属仓库名称
     */
    private String storageName;

    /**
     * 套餐标题/名称
     */
    private String title;

    /**
     * 销量 (已售出数量)
     */
    private Integer sales;

    /**
     * 评分/口碑 (通常为5分制或10分制)
     */
    private BigDecimal score;

    /**
     * 详情描述 (如：菜品介绍、配料、口味等)
     */
    private String detail;

    /**
     * 状态 (如：0-下架 1-上架)
     */
    private Integer status;

    /**
     * 套餐图片URL (主图)
     */
    private String img;

    /**
     * 所属仓库ID
     */
    private Long storageId;

}