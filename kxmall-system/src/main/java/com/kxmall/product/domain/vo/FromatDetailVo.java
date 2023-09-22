
package com.kxmall.product.domain.vo;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FromatDetailVo {
    private  String attrHidden;

    private  String detailValue;

    private List<String> detail;

    private String value;

}
