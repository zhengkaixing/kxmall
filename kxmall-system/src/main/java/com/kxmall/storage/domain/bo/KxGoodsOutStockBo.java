package com.kxmall.storage.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import com.kxmall.common.core.validate.AddGroup;
import com.kxmall.common.core.validate.EditGroup;
import com.kxmall.storage.domain.vo.KxInStockProductVo;
import com.kxmall.storage.domain.vo.KxOutStockProductVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 商品出库业务对象 kx_goods_out_stock
 *
 * @author 郅兴开源团队-小黑
 * @date 2023-08-27
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxGoodsOutStockBo extends BaseEntity {

    /**
     * 出库id
     */
    @NotNull(message = "出库id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 仓库id
     */
    private Long storageId;

    /**
     * 出库单号
     */
    private String outStockNumbers;

    /**
     * 0:待出库;1:已出库；
     */
    private Integer states;

    /**
     * 出库人
     */
    private String outgoingPerson;

    /**
     * 出库时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate outgoingTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     *
     */
    private String outgoingDay;

    /**
     * 仓库ids
     */
    private List<Long> storageIds;


    private List<KxOutStockProductVo> outStockProductVoList;
}
