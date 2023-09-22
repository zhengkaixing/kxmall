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
    private String deliveryName;
    private String deliveryId;
    private String payType;
    private Long uid;
    /**
     * 提现申请ID
     */
    private Long extractId;

    private Long employeeSn;
    private String email;
    private String name;
    private String companyName;
    private String sendTime;
    private String salaryAmt;
    private String phone;
    private Long sendId;



}
