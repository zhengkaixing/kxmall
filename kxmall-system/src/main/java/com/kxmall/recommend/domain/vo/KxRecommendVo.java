package com.kxmall.recommend.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 推荐管理视图对象 kx_recommend
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Data
@ExcelIgnoreUnannotated
public class KxRecommendVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 商品id
     */
    @ExcelProperty(value = "商品id")
    private Long productId;
    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    private String productName;

    /**
     * 推荐类型1特价推荐
     */
    @ExcelProperty(value = "推荐类型1特价推荐")
    private Long recommendType;


    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * vip价格
     */
    private BigDecimal vipPrice;

    /**
     * 销量
     */
    private Long sales;

    /**
     * 图片
     */
    private String img;


    /**
     * 标题
     */
    private String title;

    /**
     * 分类
     */
    private Long categoryId;


    /**
     * 规则id
     */
    private Long productAttrId;

    /**
     * 单位
     */
    private String unitName;





}
