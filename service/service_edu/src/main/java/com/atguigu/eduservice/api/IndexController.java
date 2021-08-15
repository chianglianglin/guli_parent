package com.atguigu.eduservice.api;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "首頁顯示")
@RestController
@RequestMapping("/eduservice/index")
@CrossOrigin
public class IndexController {

    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation(value = "首頁展示8條課程信息4條講師信息")
    @GetMapping("getCourseTeacherList")
    public R getCourseTeacherList(){
        //8條課程信息
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.orderByDesc("gmt_create");
        courseWrapper.last("LIMIT 8");

        List<EduCourse> courseList = courseService.list(courseWrapper);
        //4條講師信息
        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.orderByDesc("gmt_create");
        teacherWrapper.last("LIMIT 4");
        List<EduTeacher> teacherList = teacherService.list(teacherWrapper);
        return R.ok().data("courseList",courseList).data("teacherList",teacherList);

    }
}
