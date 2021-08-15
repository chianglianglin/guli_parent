package com.atguigu.eduservice.api;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description = "前台講師展示")
@RestController
@RequestMapping("/eduservice/teacherapi")
@CrossOrigin
public class TeacherApiController {

    @Autowired
    private EduTeacherService teacherService;
    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "前台帶條件分頁查詢講師列表")
    @GetMapping("getTeacherApiPage/{current}/{limit}")
    public R getTeacherApiPage(@PathVariable Long current,
                            @PathVariable Long limit){
        Page<EduTeacher> page = new Page<>(current, limit);
        //數據量太多才要這樣做
        Map<String,Object> map = teacherService.getTeacherApiPage(page);
        return R.ok().data(map);
    }

    @ApiOperation(value = "前台查詢講師詳情")
    @GetMapping("getTeacherCourseById/{id}")
    public R getTeacherCourseById(@PathVariable String id){
        //1.講師信息
        EduTeacher eduTeacher = teacherService.getById(id);
        //2.相關課程
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",id);
        List<EduCourse> courselist = courseService.list(queryWrapper);
        return R.ok().data("eduTeacher",eduTeacher).data("courselist",courselist);
    }
}
