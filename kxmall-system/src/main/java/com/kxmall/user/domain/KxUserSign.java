package com.kxmall.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 签到记录对象 kx_user_sign
 *
 * @author kxmall
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_user_sign")
public class KxUserSign extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 用户uid
     */
    private Long uid;
    /**
     * 签到说明
     */
    private String title;
    /**
     * 获得积分
     */
    private Long number;
    /**
     * 剩余积分
     */
    private Long balance;
    /**
     *
     */
    private Integer isDel;

}
