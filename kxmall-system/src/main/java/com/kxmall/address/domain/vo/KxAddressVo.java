package com.kxmall.address.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kxmall.common.annotation.ExcelDictFormat;
import com.kxmall.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 用户下单地址视图对象 kx_address
 *
 * @author kxmall
 * @date 2023-04-06
 */
@Data
@ExcelIgnoreUnannotated
public class KxAddressVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 省份
     */
    @ExcelProperty(value = "省份")
    private String province;

    /**
     * 城市
     */
    @ExcelProperty(value = "城市")
    private String city;

    /**
     * 区县
     */
    @ExcelProperty(value = "区县")
    private String county;

    /**
     * 详细地址
     */
    @ExcelProperty(value = "详细地址")
    private String address;

    /**
     * 默认地址
     */
    @ExcelProperty(value = "默认地址")
    private Long defaultAddress;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private Long userId;

    /**
     * 电话号码
     */
    @ExcelProperty(value = "电话号码")
    private String phone;

    /**
     * 收货人
     */
    @ExcelProperty(value = "收货人")
    private String consignee;

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


}
