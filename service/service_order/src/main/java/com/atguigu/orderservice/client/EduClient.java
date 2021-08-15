package com.atguigu.orderservice.client;


import com.atguigu.commonutils.vo.CourseWebVoForOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-edu")
public interface EduClient {
    //根據課程id獲取課程相關信息跨模塊
    @GetMapping("/eduservice/courseapi/getCourseInfoForOrder/{courseId}")
    public CourseWebVoForOrder getCourseInfoForOrder(@PathVariable("courseId") String courseId);

    }
