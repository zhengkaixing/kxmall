package com.kxmall.finance.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class StorageCompareReportDto {


    private Map<String, List<BigDecimal>> dataList;

    private List<Date> timeList;
}
