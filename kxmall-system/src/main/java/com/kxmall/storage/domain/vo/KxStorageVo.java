package com.kxmall.storage.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kxmall.common.annotation.ExcelDictFormat;
import com.kxmall.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 仓库管理视图对象 kx_storage
 *
 * @author kxmall
 * @date 2023-08-27
 */
@Data
@ExcelIgnoreUnannotated
public class KxStorageVo {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库主键ID
     */
    @ExcelProperty(value = "仓库主键ID")
    private Long id;

    /**
     * 仓库名称
     */
    @ExcelProperty(value = "仓库名称")
    private String name;

    /**
     * 省
     */
    @ExcelProperty(value = "省")
    private Long province;

    /**
     * 市
     */
    @ExcelProperty(value = "市")
    private Long city;

    /**
     * 区（县）
     */
    @ExcelProperty(value = "区", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "县=")
    private Long county;

    /**
     * 详细地址
     */
    @ExcelProperty(value = "详细地址")
    private String address;

    /**
     * 区域编码
     */
    @ExcelProperty(value = "区域编码")
    private String adcode;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 状态 0.禁用 1.正常
     */
    @ExcelProperty(value = "状态 0.禁用 1.正常")
    private Integer state;

    /**
     * 经度
     */
    @ExcelProperty(value = "经度")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @ExcelProperty(value = "纬度")
    private BigDecimal latitude;

    /**
     * 仓库管理电话
     */
    @ExcelProperty(value = "仓库管理电话")
    private String phone;

    /**
     * 仓库管理名称
     */
    @ExcelProperty(value = "仓库管理名称")
    private String leaderName;

    /**
     * 营业状态 0.休息 1.营业
     */
    @ExcelProperty(value = "营业状态 0.休息 1.营业")
    private Integer operatingState;

    /**
     * 营业起始时间
     */
    @ExcelProperty(value = "营业起始时间")
    private String businessStartTime;

    /**
     * 配送起始时间
     */
    @ExcelProperty(value = "配送起始时间")
    private String deliveryStartTime;

    /**
     * 营业结束时间
     */
    @ExcelProperty(value = "营业结束时间")
    private String businessStopTime;

    /**
     * 配送结束时间
     */
    @ExcelProperty(value = "配送结束时间")
    private String deliveryStopTime;

    /**
     * 配送范围
     */
    @ExcelProperty(value = "配送范围")
    private Long deliveryRadius;

    /**
     * 是否自动分配订单【0：非自动 1：自动】
     */
    @ExcelProperty(value = "是否自动分配订单【0：非自动 1：自动】")
    private Integer automatic;

    /**
     * 状态 0.禁用 1.正常
     */
    @ExcelProperty(value = "状态 0.禁用 1.正常")
    private Integer printSwitch;

    /**
     * 账号名
     */
    @ExcelProperty(value = "账号名")
    private String printAcount;

    /**
     * Ukey
     */
    @ExcelProperty(value = "Ukey")
    private String printUkey;

    /**
     * SN
     */
    @ExcelProperty(value = "SN")
    private String printSn;


    private Date updateTime;

    private Date createTime;

    /**
     * 距离
     */
    private BigDecimal distance;

}
