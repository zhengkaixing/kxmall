package com.kxmall.storage.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: kxmall
 * @date: 2020/03/08 14:25
 **/
@Data
public class RoleStorageVo implements Serializable {


    private Long roleId;

    private List<Long> storageIdList;

}
