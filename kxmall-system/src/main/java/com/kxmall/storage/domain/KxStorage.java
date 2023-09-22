package com.kxmall.storage.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 仓库管理对象 kx_storage
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_storage")
public class KxStorage extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 仓库主键ID
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 仓库名称
     */
    private String name;
    /**
     * 省
     */
    private Long province;
    /**
     * 市
     */
    private Long city;
    /**
     * 区（县）
     */
    private Long county;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 区域编码
     */
    private String adcode;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态 0.禁用 1.正常
     */
    private Integer state;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 仓库管理电话
     */
    private String phone;
    /**
     * 仓库管理名称
     */
    private String leaderName;
    /**
     * 营业状态 0.休息 1.营业
     */
    private Integer operatingState;
    /**
     * 营业起始时间
     */
    private String businessStartTime;
    /**
     * 配送起始时间
     */
    private String deliveryStartTime;
    /**
     * 营业结束时间
     */
    private String businessStopTime;
    /**
     * 配送结束时间
     */
    private String deliveryStopTime;
    /**
     * 配送范围
     */
    private Long deliveryRadius;
    /**
     * 是否自动分配订单【0：非自动 1：自动】
     */
    private Integer automatic;
    /**
     * 状态 0.禁用 1.正常
     */
    private Integer printSwitch;
    /**
     * 账号名
     */
    private String printAcount;
    /**
     * Ukey
     */
    private String printUkey;
    /**
     * SN
     */
    private String printSn;

}
