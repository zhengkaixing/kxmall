package com.kxmall.newtimes.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;


/**
 * 新鲜时报业务对象 kx_new_times
 *
 * @author kxmall
 * @date 2023-10-05
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxNewTimesBo extends BaseEntity {

    /**
     * 时报id
     */
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

    /**
     * 仓库权限参数
     */
    private Set<Long> storageIds;
}
