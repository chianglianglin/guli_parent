package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
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
@Api(description = "章節管理")
@RestController
@RequestMapping("/eduservice/educhapter")

public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;



    @ApiOperation(value="根據課程id查詢章節,小節信息")
    @GetMapping("getChapterVideoById/{courseId}")
    public R getChapterVideoById(@PathVariable String courseId){
        List<ChapterVo> ChapterVoList = chapterService.getChapterVideoById(courseId);
        return R.ok().data("chapterVideoList",ChapterVoList);
    }

    @ApiOperation(value="添加章節")
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return R.ok();
    }

    @ApiOperation(value="根據id刪除章節")
    @DeleteMapping("delChapter/{id}")
    public R delChapter(@PathVariable String id){
        chapterService.removeById(id);
        return R.ok();
    }
    @ApiOperation(value="根據id查詢章節")
    @GetMapping("getChapter/{id}")
    public R getChapter(@PathVariable String id){
        EduChapter eduChapter = chapterService.getById(id);
        return R.ok().data("eduChapter",eduChapter);
    }

    @ApiOperation(value="修改章節")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return R.ok();
    }
}

