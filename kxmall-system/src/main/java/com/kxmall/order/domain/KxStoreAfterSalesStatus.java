package com.kxmall.order.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 售后订单操作详情对象 kx_store_after_sales_status
 *
 * @author kxmall
 * @date 2024-11-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_after_sales_status")
public class KxStoreAfterSalesStatus extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 售后id
     */
    private Long storeAfterSalesId;
    /**
     * 操作类型
     */
    private Integer changeType;
    /**
     * 操作备注
     */
    private String changeMessage;
    /**
     * 操作时间
     */
    private Date changeTime;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 删除状态
     */
    private Integer isDel;

}
