package com.atguigu.eduservice.api;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.CourseWebVoForOrder;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description = "前台課程展示")
@RestController
@RequestMapping("/eduservice/courseapi")
@CrossOrigin
public class CourseApiController {

    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;

    @ApiOperation(value = "帶條件分頁查詢課程列表")
    @PostMapping("getCourseApiPageVo/{current}/{limit}")
    public R getCourseApiPageVo(@PathVariable Long current,
                            @PathVariable Long limit,
                            @RequestBody CourseQueryVo courseQueryVo){
        Page<EduCourse> page = new Page<>(current,limit);
        Map<String,Object> map = courseService.getCourseApiPageVo(page,courseQueryVo);
        return R.ok().data(map);
    }
    @ApiOperation(value = "根據課程id課程相關信息")
    @GetMapping("getCourseWebInfo/{courseId}")
    public R getCourseWebInfo(@PathVariable String courseId){
        //1.查詢課程相關信息存入coursewebvo
        CourseWebVo courseWebVo = courseService.getCourseWebVo(courseId);
        //2.查詢課程大綱信息
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoById(courseId);
        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList);
    }

    @ApiOperation(value = "根據課程id課程相關信息跨模塊")
    @GetMapping("getCourseInfoForOrder/{courseId}")
    public CourseWebVoForOrder getCourseInfoForOrder(@PathVariable("courseId") String courseId){
        //1.查詢課程相關信息存入coursewebvo
        CourseWebVo courseWebVo = courseService.getCourseWebVo(courseId);
        CourseWebVoForOrder courseWebVoForOrder = new CourseWebVoForOrder();
        BeanUtils.copyProperties(courseWebVo,courseWebVoForOrder);
        return courseWebVoForOrder;
    }

}
