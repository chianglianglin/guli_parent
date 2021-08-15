package com.atguigu.staservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.staservice.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author jackson
 * @since 2021-08-15
 */
@Api(description="統計分析管理")
@RefreshScope
@RestController
@RequestMapping("/staservice/stadaily")
@CrossOrigin

public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService dailyService;

    @ApiOperation(value="生成統計數據")
    @PostMapping("createStaDaily/{day}")
    public R createStaDaily(@PathVariable String day){
        dailyService.createStaDaily(day);
        return R.ok();
    }

    @ApiOperation(value="查詢統計數據")
    @GetMapping("getStaDaily/{type}/{begin}/{end}")
    public R getStaDaily(@PathVariable String type,
                         @PathVariable String begin,
                         @PathVariable String end){
        Map<String ,Object> map = dailyService.getStaDaily(type,begin,end);
        return R.ok().data(map);
    }

}

