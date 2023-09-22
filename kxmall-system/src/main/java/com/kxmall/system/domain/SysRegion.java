package com.kxmall.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 中国地区系统对象 sys_region
 *
 * @author kxmall
 * @date 2023-02-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_region")
public class SysRegion extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 省市级别
     */
    private Long level;
    /**
     * 父级id
     */
    private Long parentId;
    /**
     * 区号
     */
    private String areaCode;
    /**
     * 名称
     */
    private String name;
    /**
     * 合并名称
     */
    private String mergerName;
    /**
     * 经度
     */
    private String lng;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 是否展示
     */
    private Integer isShow;

}
