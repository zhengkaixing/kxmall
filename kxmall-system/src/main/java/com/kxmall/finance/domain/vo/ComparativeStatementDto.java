package com.kxmall.finance.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ComparativeStatementDto implements Serializable {


    private String date;
    private BigDecimal content;
    private String storageName;
}
