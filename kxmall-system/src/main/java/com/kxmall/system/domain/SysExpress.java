package com.kxmall.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 快递公司对象 sys_express
 *
 * @author kxmall
 * @date 2023-02-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_express")
public class SysExpress extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 快递公司id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 快递公司简称
     */
    private String code;
    /**
     * 快递公司全称
     */
    private String name;
    /**
     * 排序
     */
    private Long sort;
    /**
     * 是否显示
     */
    private Integer isShow;
    /**
     *
     */
    private Long isDel;

}
