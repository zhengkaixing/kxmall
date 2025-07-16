package com.kxmall.order.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 售后订单操作详情视图对象 kx_store_after_sales_status
 *
 * @author kxmall
 * @date 2024-11-23
 */
@Data
@ExcelIgnoreUnannotated
public class KxStoreAfterSalesStatusVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 售后id
     */
    @ExcelProperty(value = "售后id")
    private Long storeAfterSalesId;

    /**
     * 操作类型
     */
    @ExcelProperty(value = "操作类型")
    private Integer changeType;

    /**
     * 操作备注
     */
    @ExcelProperty(value = "操作备注")
    private String changeMessage;

    /**
     * 操作时间
     */
    @ExcelProperty(value = "操作时间")
    private Date changeTime;

    /**
     * 操作人
     */
    @ExcelProperty(value = "操作人")
    private String operator;

    /**
     * 删除状态
     */
    @ExcelProperty(value = "删除状态")
    private Integer isDel;


}
