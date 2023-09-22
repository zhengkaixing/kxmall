package com.kxmall.user.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 用户账单对象 kx_user_bill
 *
 * @author kxmall
 * @date 2023-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_user_bill")
@Builder
public class KxUserBill extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 用户账单id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 用户uid
     */
    private Long uid;
    /**
     * 关联id
     */
    private String linkId;
    /**
     * 0 = 支出 1 = 获得
     */
    private Integer pm;
    /**
     * 账单标题
     */
    private String title;
    /**
     * 明细种类
     */
    private String category;
    /**
     * 明细类型
     */
    private String type;
    /**
     * 明细数字
     */
    private BigDecimal number;
    /**
     * 剩余
     */
    private BigDecimal balance;
    /**
     * 备注
     */
    private String mark;
    /**
     * 0 = 带确定 1 = 有效 -1 = 无效
     */
    private Integer status;
    /**
     *
     */
    private Integer isDel;

}
