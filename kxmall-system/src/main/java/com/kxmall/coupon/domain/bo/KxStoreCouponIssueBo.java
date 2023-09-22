package com.kxmall.coupon.domain.bo;

import com.kxmall.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 优惠券前台领取业务对象 kx_store_coupon_issue
 *
 * @author kxmall
 * @date 2023-02-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class KxStoreCouponIssueBo extends BaseEntity {

    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private String cname;

    /**
     * 优惠券ID
     */
    private Integer cid;

    /**
     * 优惠券类型 0-通用 1-商品券
     */
    private Integer ctype;

    /**
     * 优惠券领取开启时间
     */
    private Date startTime;

    /**
     * 优惠券领取结束时间
     */
    private Date endTime;

    /**
     * 优惠券领取数量
     */
    private Integer totalCount;

    /**
     * 优惠券剩余领取数量
     */
    private Integer remainCount;

    /**
     * 是否无限张数
     */
    private Integer isPermanent;

    /**
     * 1 正常 0 未开启 -1 已无效
     */
    private Integer status;

    /**
     *
     */
    private Integer isDel;


}
