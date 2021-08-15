package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author jackson
 * @since 2021-08-15
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {


    @Autowired
    private UcenterClient ucenterClient;
    @Override
    public void createStaDaily(String day) {
        //1.刪除數據
        QueryWrapper<StatisticsDaily> queryWrapperdel = new QueryWrapper<>();
        queryWrapperdel.eq("date_calculated",day);
        baseMapper.delete(queryWrapperdel);
        //2.統計數據
        R r = ucenterClient.countRegister(day);
        Integer registerNum = (Integer)r.getData().get("countRegister");
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO
        //3.封裝
        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);
        baseMapper.insert(daily);

    }
        //查詢統計數據
    @Override
    public Map<String, Object> getStaDaily(String type, String begin, String end) {
        //查詢數據
        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("date_calculated",begin,end);
        queryWrapper.select("date_calculated",type);
        List<StatisticsDaily> dailieList = baseMapper.selectList(queryWrapper);
        //遍歷查詢數據
        Map<String, Object> staDailyMap = new HashMap<>();
        List<String> dateCalculatedList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();
         for (int i = 0; i < dailieList.size(); i++) {
             StatisticsDaily daily = dailieList.get(i);
            //封裝x軸數據
             dateCalculatedList.add(daily.getDateCalculated());
             //封裝y軸數據
             switch (type){
                 case "register_num":
                     dataList.add(daily.getRegisterNum());
                     break;
                 case "login_num":
                     dataList.add(daily.getLoginNum());
                     break;
                 case "video_view_num":
                     dataList.add(daily.getVideoViewNum());
                     break;
                 case "course_num":
                     dataList.add(daily.getCourseNum());
                     break;
                 default:
                     break;}
             }
        staDailyMap.put("dateCalculatedList",dateCalculatedList);
        staDailyMap.put("dataList",dataList);


        return staDailyMap;

    }
}
