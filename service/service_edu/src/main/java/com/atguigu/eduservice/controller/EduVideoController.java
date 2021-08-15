package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author jackson
 * @since 2021-08-06
 */
@Api(description = "小節管理")
@RestController
@RequestMapping("/eduservice/eduvideo")
//@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    @ApiOperation(value="添加小節")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }

    //刪除小結時同時刪除阿里雲視頻
    @ApiOperation(value="根據id刪除小節")
    @DeleteMapping("delVideo/{id}")
    public R delVideo(@PathVariable String id){
        EduVideo eduVideo = videoService.getById(id);
        String videoId = eduVideo.getVideoSourceId();
        if(videoId!=null){
            vodClient.delVideo(videoId);
        }

        videoService.removeById(id);
        return R.ok();
    }
    @ApiOperation(value="根據id查詢小節")
    @GetMapping("getVideoById/{id}")
    public R getVideoById(@PathVariable String id){
        EduVideo eduVideo = videoService.getById(id);
        return R.ok().data("eduVideo",eduVideo);
    }

    @ApiOperation(value="修改小節")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        videoService.updateById(eduVideo);
        return R.ok();
    }

}

