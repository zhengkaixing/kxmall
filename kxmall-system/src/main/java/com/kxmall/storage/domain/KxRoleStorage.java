package com.kxmall.storage.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: kxmall
 * @date: 2020/03/08 14:21
 **/
@Data
@TableName("kx_role_storage")
public class KxRoleStorage implements Serializable {

    @TableField("role_id")
    private Long roleId;

    @TableField("storage_id")
    private Long storageId;

}
