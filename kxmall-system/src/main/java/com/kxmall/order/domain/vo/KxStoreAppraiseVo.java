package com.kxmall.order.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kxmall.common.annotation.ExcelDictFormat;
import com.kxmall.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 评论管理视图对象 kx_store_appraise
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreAppraiseVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
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
     * 规格id
     */
    @ExcelProperty(value = "规格id")
    private Long productAttrId;

    /**
     * 规格名称
     */
    @ExcelProperty(value = "规格名称")
    private String productAttrName;

    /**
     * 订单id
     */
    @ExcelProperty(value = "订单id")
    private Long orderId;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private Long userId;

    /**
     * 评论内容
     */
    @ExcelProperty(value = "评论内容")
    private String content;

    /**
     * 打分
     */
    @ExcelProperty(value = "打分")
    private Long score;

    /**
     * 1表示已通过
     */
    @ExcelProperty(value = "1表示已通过")
    private Long state;


    private Date createTime;

    private Date updateTime;

    /**
     * 用户昵称
     */
    private String userNickName;
    /**
     * 头像
     */
    private String userAvatarUrl;


}
