package com.atguigu.staservice.scheduled;


import com.atguigu.staservice.service.StatisticsDailyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class ScheduledTask {
    @Autowired
    private StatisticsDailyService dailyService;

//    @Scheduled(cron = "0/5 * * * * ?")
//    public void tesk1(){
//        System.out.println("task1------------");
//    }

//    //每天凌晨一點跑前一天數據
//    @Scheduled(cron = "1 1 1 * * ? ")
//    public void tesk2(){
//        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
//        dailyService.createStaDaily(day);
//        System.out.println("生成數據成功"+day);
//    }

}
