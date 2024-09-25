package com.kxmall.web.controller.dashboard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kxmall.common.enums.OrderStatusType;
import com.kxmall.dashboard.domain.OrderTimeDataDTO;
import com.kxmall.dashboard.domain.SalesStatementDTO;
import com.kxmall.dashboard.domain.SalesTopDTO;
import com.kxmall.dashboard.domain.UserStatementDTO;
import com.kxmall.order.domain.KxStoreOrder;
import com.kxmall.order.domain.vo.OrderTimeDataVo;
import com.kxmall.order.mapper.KxStoreOrderMapper;
import com.kxmall.product.mapper.KxStoreProductMapper;
import com.kxmall.storage.domain.KxStock;
import com.kxmall.storage.mapper.KxStockMapper;
import com.kxmall.user.mapper.KxUserMapper;
import com.kxmall.web.controller.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by admin on 2019/7/15.
 */
@Service
@SuppressWarnings("all")
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private KxStoreOrderMapper orderMapper;

    @Autowired
    private KxStoreProductMapper storeProductMapper;

    @Autowired
    private KxUserMapper userMapper;

    @Autowired
    private KxStockMapper stockMapper;
    /**
     * 用户数量统计
     *
     * @param adminId
     * @return
     * @
     */

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<UserStatementDTO> countUser() {
        List<UserStatementDTO> userStatements = new ArrayList<>();
        UserStatementDTO firstDay = new UserStatementDTO();
        UserStatementDTO secondDay = new UserStatementDTO();
        UserStatementDTO thirdDay = new UserStatementDTO();
        UserStatementDTO fourthDay = new UserStatementDTO();
        UserStatementDTO fifthDay = new UserStatementDTO();
        UserStatementDTO sixthDay = new UserStatementDTO();
        UserStatementDTO seventhDay = new UserStatementDTO();
        userStatements.add(firstDay);
        userStatements.add(secondDay);
        userStatements.add(thirdDay);
        userStatements.add(fourthDay);
        userStatements.add(fifthDay);
        userStatements.add(sixthDay);
        userStatements.add(seventhDay);
        UserStatementDTO userStatementDTO;
        for (int i = 0; i < 7; i++) {
            Date[] dates = getDate(i);
            userStatementDTO = userStatements.get(i);
            if (i == 0) {
                userStatementDTO.setStatementDate("今日");
            } else if (i == 1) {
                userStatementDTO.setStatementDate("昨日");
            } else {
                String format = simpleDateFormat.format(dates[0]);
                userStatementDTO.setStatementDate(format);
            }
            Long aLong = Long.valueOf(i + "");
            userStatementDTO.setId(aLong);
            Integer countTotalUser = userMapper.countTotalUser(dates[0]);
            userStatementDTO.setTotalUser(countTotalUser);
            Integer countNewUser = userMapper.countNewUser(dates[0], dates[1]);
            userStatementDTO.setNewUser(countNewUser);
            Integer countOnlineUser = userMapper.countOnlineUser(dates[0], dates[1]);
            userStatementDTO.setOnlineUser(countOnlineUser);
            Integer countOrderUser = userMapper.countOrderUser(dates[0], dates[1]);
            userStatementDTO.setOrderUser(countOrderUser);
            Integer countFirstOrderUser = userMapper.countFirstOrderUser(dates[0], dates[1]);
            userStatementDTO.setFirstOrderUser(countFirstOrderUser);
        }
        return userStatements;
    }

    @Override
    public List<SalesStatementDTO> getSalesStatement(Long storageId, Set<Long> storagePermission) {

        List<SalesStatementDTO> salesCategoryRank = orderMapper.getSalesCategoryRank(storageId,storagePermission);
        for (SalesStatementDTO salesStatementDTO : salesCategoryRank) {
            Long categoryId = salesStatementDTO.getCategoryId();
            List<SalesTopDTO> salesCategoryRanTopFive = orderMapper.getSalesCategoryRanTopFive(categoryId, storageId,storagePermission);
            salesStatementDTO.setSalesTopDTOs(salesCategoryRanTopFive);
        }
        return salesCategoryRank;
    }

    @SuppressWarnings("all")
    public static Date[] getDate(int past) {
        Date[] dates = new Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        dates[0] = start;
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date end = calendar.getTime();
        dates[1] = end;
        return dates;
    }

    public static Date[] getTwoHourDate(int past) {
        Date[] dates = new Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.HOUR_OF_DAY, (past - 1) * 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        dates[0] = start;
        calendar.set(Calendar.HOUR_OF_DAY, past * 2 - 1);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date end = calendar.getTime();
        dates[1] = end;
        return dates;
    }


    @Override
    public List<SalesStatementDTO> getTodayAndYesterdaySales(Long storageId, Set<Long> storagePermission) {
        List<SalesStatementDTO> salesStatementDTOS = new ArrayList<>();
        SalesStatementDTO todaySales = orderMapper.getTodaySales(storageId,storagePermission);
        SalesStatementDTO yesterdaySales = orderMapper.getYesterdaySales(storageId,storagePermission);
        if (todaySales == null) {
            todaySales = new SalesStatementDTO();
        }
        if (yesterdaySales == null) {
            yesterdaySales = new SalesStatementDTO();
        }
        salesStatementDTOS.add(todaySales);
        salesStatementDTOS.add(yesterdaySales);
        return salesStatementDTOS;
    }


    @Override
    public List<SalesStatementDTO> getSalesByHour(Long storageId, Set<Long> storagePermission) {
        int hour = getHour(new Date()) / 2 + 1;
        List<SalesStatementDTO> salesStatementDTOS = new ArrayList<>();
        for (int i = 1; i <= hour; i++) {
            Date[] twoHourDate = getTwoHourDate(i);
            SalesStatementDTO salesByHour = orderMapper.getSalesByHour(storageId, twoHourDate[0], twoHourDate[1],storagePermission);
            salesByHour.setId(Long.valueOf(i + ""));
            salesStatementDTOS.add(salesByHour);
        }
        return salesStatementDTOS;
    }

    @Override
    public OrderTimeDataDTO getCount(Long storageId) {
        OrderTimeDataDTO orderTimeDataDto = new OrderTimeDataDTO();

        OrderTimeDataVo shoperOrderTimeData = this.getShoperOrderTimeData(storageId);

        BeanUtil.copyProperties(shoperOrderTimeData, orderTimeDataDto);

        orderTimeDataDto.setUserCount(userMapper.selectCount(new LambdaQueryWrapper<>()));
        orderTimeDataDto.setOrderCount(orderMapper.selectCount(new LambdaQueryWrapper<KxStoreOrder>().eq(storageId!=null,KxStoreOrder::getStoreId,storageId)));
        orderTimeDataDto.setPriceCount(orderMapper.sumTotalPrice(storageId));

        if (ObjectUtils.isEmpty(storageId)) {
            orderTimeDataDto.setGoodsCount(storeProductMapper.selectCount(new LambdaQueryWrapper<>()));
        }else {
            orderTimeDataDto.setGoodsCount(stockMapper.selectCount(new LambdaQueryWrapper<KxStock>().eq(storageId!=null,KxStock::getStorageId,storageId)));
        }


        return orderTimeDataDto;
    }

    /**
     * 获取 今日 昨日 本月 订单金额
     * @return ShoperOrderTimeDataVo
     * @param storageId
     */
    public OrderTimeDataVo getShoperOrderTimeData(Long storageId) {

        Date today = DateUtil.beginOfDay(new Date());
        Date yesterday = DateUtil.beginOfDay(DateUtil.yesterday());
        Date nowMonth = DateUtil.beginOfMonth(new Date());
        Date lastWeek = DateUtil.beginOfDay(DateUtil.lastWeek());

        OrderTimeDataVo orderTimeDataVo = new OrderTimeDataVo();

        //今日成交额
        LambdaQueryWrapper<KxStoreOrder> wrapperOne = new LambdaQueryWrapper<>();
        wrapperOne
                .ge(KxStoreOrder::getPayTime, today)
                .isNotNull(KxStoreOrder::getPayTime)
                .eq(storageId!=null,KxStoreOrder::getStoreId,storageId)
                .in(KxStoreOrder::getStatus, OrderStatusType.COMPLETE.getCode(),OrderStatusType.WAIT_APPRAISE.getCode());
        orderTimeDataVo.setTodayPrice(orderMapper.todayPrice(wrapperOne));
        //今日订单数
        orderTimeDataVo.setTodayCount(orderMapper.selectCount(wrapperOne));

        //昨日成交额
        LambdaQueryWrapper<KxStoreOrder> wrapperTwo = new LambdaQueryWrapper<>();
        wrapperTwo
                .lt(KxStoreOrder::getPayTime, today)
                .ge(KxStoreOrder::getPayTime, yesterday)
                .eq(storageId!=null,KxStoreOrder::getStoreId,storageId)
                .in(KxStoreOrder::getStatus, OrderStatusType.COMPLETE.getCode(),OrderStatusType.WAIT_APPRAISE.getCode());
        orderTimeDataVo.setProPrice(orderMapper.todayPrice(wrapperTwo));
        //昨日订单数
        orderTimeDataVo.setProCount(orderMapper.selectCount(wrapperTwo));

        //本月成交额
        LambdaQueryWrapper<KxStoreOrder> wrapperThree = new LambdaQueryWrapper<>();
        wrapperThree
                .ge(KxStoreOrder::getPayTime, nowMonth)
                .eq(storageId!=null,KxStoreOrder::getStoreId,storageId)
                .in(KxStoreOrder::getStatus, OrderStatusType.COMPLETE.getCode(),OrderStatusType.WAIT_APPRAISE.getCode());
        orderTimeDataVo.setMonthPrice(orderMapper.todayPrice(wrapperThree));
        //本月订单数
        orderTimeDataVo.setMonthCount(orderMapper.selectCount(wrapperThree));

        //上周成交额
        LambdaQueryWrapper<KxStoreOrder> wrapperLastWeek = new LambdaQueryWrapper<>();
        wrapperLastWeek
                .lt(KxStoreOrder::getPayTime, today)
                .ge(KxStoreOrder::getPayTime, lastWeek)
                .eq(storageId!=null,KxStoreOrder::getStoreId,storageId)
                .in(KxStoreOrder::getStatus, OrderStatusType.COMPLETE.getCode(),OrderStatusType.WAIT_APPRAISE.getCode());
        orderTimeDataVo.setLastWeekPrice(orderMapper.todayPrice(wrapperLastWeek));
        //上周订单数
        orderTimeDataVo.setLastWeekCount(orderMapper.selectCount(wrapperLastWeek));

        return orderTimeDataVo;
    }

    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static void main(String[] args) {
        System.out.println(getHour(new Date()) / 2);
    }
}
