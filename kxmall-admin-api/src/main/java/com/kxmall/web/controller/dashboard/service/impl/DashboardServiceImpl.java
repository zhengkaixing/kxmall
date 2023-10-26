package com.kxmall.web.controller.dashboard.service.impl;

import com.kxmall.dashboard.domain.SalesStatementDTO;
import com.kxmall.dashboard.domain.SalesTopDTO;
import com.kxmall.dashboard.domain.UserStatementDTO;
import com.kxmall.order.mapper.KxStoreOrderMapper;
import com.kxmall.product.mapper.KxStoreProductMapper;
import com.kxmall.user.mapper.KxUserMapper;
import com.kxmall.web.controller.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static void main(String[] args) {
        System.out.println(getHour(new Date()) / 2);
    }
}
