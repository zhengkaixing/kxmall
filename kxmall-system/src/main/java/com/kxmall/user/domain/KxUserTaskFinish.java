package com.kxmall.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("kx_user_task_finish")
public class KxUserTaskFinish {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long taskId;
    private Long uid;
    private Integer status;
    private Date createTime;
}