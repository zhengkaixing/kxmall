package com.kxmall.common.event;

import lombok.*;

/**
 * @ClassName TemplateBean
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateBean {

    private String templateType;
    private String orderId;
    private String time;
    private String price;
    private String name;
    private Long storeId;



    private Long riderId;
    private String address;
    private String freightPrice;

}
