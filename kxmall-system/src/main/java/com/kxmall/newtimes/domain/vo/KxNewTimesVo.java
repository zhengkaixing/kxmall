package com.kxmall.newtimes.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 新鲜时报视图对象 kx_new_times
 *
 * @author kxmall
 * @date 2023-10-05
 */
@Data
@ExcelIgnoreUnannotated
public class KxNewTimesVo {

    private static final long serialVersionUID = 1L;

    /**
     * 时报id
     */
    @ExcelProperty(value = "时报id")
    private Long id;

    /**
     * 仓库id
     */
    @ExcelProperty(value = "仓库id")
    private Long storageId;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    private String content;

    /**
     * 编辑人
     */
    @ExcelProperty(value = "编辑人")
    private String createBy;

    /**
     * 更新人
     */
    @ExcelProperty(value = "更新人")
    private String updateBy;

    /**
     * 时报状态0，没暂停；1，暂停
     */
    @ExcelProperty(value = "时报状态0，没暂停；1，暂停")
    private Integer isStop;

    /**
     * 编辑时间
     */
    @ExcelProperty(value = "编辑时间")
    private Date updateTime;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;


}
