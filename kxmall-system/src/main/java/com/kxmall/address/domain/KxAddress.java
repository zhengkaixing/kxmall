package com.kxmall.address.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 用户下单地址对象 kx_address
 *
 * @author kxmall
 * @date 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_address")
public class KxAddress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 区县
     */
    private String county;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 默认地址
     */
    private Integer defaultAddress;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 收货人
     */
    private String consignee;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     */
    private BigDecimal latitude;

}
