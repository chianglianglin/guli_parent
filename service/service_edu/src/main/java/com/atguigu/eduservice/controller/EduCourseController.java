package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author jackson
 * @since 2021-08-06
 */
@Api(description = "課程管理")
@RestController
@RequestMapping("/eduservice/educourse")

public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value="添加課程信息")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoForm courseInfoForm){
        String courseId = courseService.addCourseInfo(courseInfoForm);
        return R.ok().data("courseId",courseId);
    }

    @ApiOperation(value="根據id查詢課程信息")
    @GetMapping("getCourseInfoById/{id}")
    public R getCourseInfoById(@PathVariable String id){
        CourseInfoForm courseInfoForm = courseService.getCourseInfoById(id);
        return R.ok().data("courseInfo",courseInfoForm);
    }
    @ApiOperation(value="修改課程信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoForm courseInfoForm){
        courseService.updateCourseInfo(courseInfoForm);
        return R.ok();
    }
    @ApiOperation(value="根據課程id查詢課程發布信息")
    @GetMapping("getCoursePublishById/{id}")
    public R getCoursePublishById(@PathVariable String id){
        CoursePublishVo coursePublishVo =
                courseService.getCoursePublishById(id);
        return R.ok().data("coursePublishVo",coursePublishVo);
    }

    @ApiOperation(value="根據id發布課程")
    @PutMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = courseService.getById(id);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }
    @ApiOperation(value="查詢所有課程信息")
    @GetMapping("getCourseInfo")
    //TODO 實現帶條件,帶分頁查詢
    public R getCourseInfo(){
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("list",list);
    }

    @ApiOperation(value="根據id刪除課程相關信息")
    @DeleteMapping("delCourseInfo/{id}")
    public R delCourseInfo(@PathVariable String id){
        courseService.delCourseInfo(id);
        return R.ok();
    }



}

