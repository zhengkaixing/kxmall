package com.kxmall.newtimes.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 新鲜时报对象 kx_new_times
 *
 * @author kxmall
 * @date 2023-10-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_new_times")
public class KxNewTimes extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 时报id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 仓库id
     */
    private Long storageId;
    /**
     * 内容
     */
    private String content;
    /**
     * 时报状态0，没暂停；1，暂停
     */
    private Integer isStop;

}
