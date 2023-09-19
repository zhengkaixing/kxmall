package com.kxmall.region.domain.bo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * 中国地区信息业务对象 kx_region
 *
 * @author kxmall
 * @date 2023-08-27
 */

@Data
public class KxRegionBo {

    /**
     * 自增ID
     */
    @NotNull(message = "自增ID不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 代码
     */
    @NotBlank(message = "代码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 简称
     */
    @NotBlank(message = "简称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String shortName;

    /**
     * 上级代码
     */
    @NotBlank(message = "上级代码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String superiorCode;

    /**
     * 经度
     */
    @NotBlank(message = "经度不能为空", groups = {AddGroup.class, EditGroup.class})
    private String lng;

    /**
     * 纬度
     */
    @NotBlank(message = "纬度不能为空", groups = {AddGroup.class, EditGroup.class})
    private String lat;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer sort;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = {AddGroup.class, EditGroup.class})
    private String ramark;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long state;

    /**
     * 租户ID
     */
    @NotBlank(message = "租户ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String tenantCode;

    /**
     * 级别
     */
    @NotNull(message = "级别不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long level;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
