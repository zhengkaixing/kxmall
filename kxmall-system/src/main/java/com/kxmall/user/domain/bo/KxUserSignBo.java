package com.kxmall.user.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 签到记录业务对象 kx_user_sign
 *
 * @author kxmall
 * @date 2024-08-26
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxUserSignBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户uid
     */
    @NotNull(message = "用户uid不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long uid;

    /**
     * 签到说明
     */
    @NotBlank(message = "签到说明不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 获得积分
     */
    @NotNull(message = "获得积分不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long number;

    /**
     * 剩余积分
     */
    @NotNull(message = "剩余积分不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long balance;

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer isDel;


    private String nickname;
}
