package com.kxmall.user.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kxmall.user.domain.vo.KxPromUserVo;
import com.kxmall.user.domain.vo.KxUserVo;
import com.kxmall.user.domain.KxUser;
import com.kxmall.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper接口
 *
 * @author kxmall
 * @date 2023-02-14
 */
public interface KxUserMapper extends BaseMapperPlus<KxUserMapper, KxUser, KxUserVo> {


    @Select("<script>SELECT u.uid,u.nickname,u.avatar,DATE_FORMAT(u.create_time,'%Y/%m/%d') as time," +
        "u.spread_count as childCount,COUNT(o.id) as orderCount," +
        "IFNULL(SUM(o.pay_price),0) as numberCount FROM kx_user u " +
        "LEFT JOIN kx_store_order o ON u.uid=o.uid " +
        "WHERE u.uid in <foreach item='id' index='index' collection='uids' " +
        " open='(' separator=',' close=')'>" +
        "   #{id}" +
        " </foreach> <if test='keyword != null'>" +
        " AND ( u.nickname LIKE CONCAT(CONCAT('%',#{keyword}),'%') OR u.phone LIKE CONCAT(CONCAT('%',#{keyword}),'%'))</if>" +
        " GROUP BY u.uid ORDER BY #{orderByStr} " +
        "</script>")
    List<KxPromUserVo> getUserSpreadCountList(Page page,
                                              @Param("uids") List uids,
                                              @Param("keyword") String keyword,
                                              @Param("orderByStr") String orderBy);
}
