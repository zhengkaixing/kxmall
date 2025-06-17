package com.kxmall.storage.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import com.kxmall.common.core.type.JsonTypeHandler;
import com.kxmall.storage.domain.bo.PointBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 仓库管理对象 kx_storage
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "kx_storage",autoResultMap = true)
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
    /**
     * 公众号openId
     */
    private String openId;

    /**
     * 定位范围
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<List<PointBo>> paths;

}
