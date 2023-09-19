
package com.kxmall.product.domain.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DetailVo {

    private List<String> data;

    private List<Map<String,Map<String,String>>> res;
}
