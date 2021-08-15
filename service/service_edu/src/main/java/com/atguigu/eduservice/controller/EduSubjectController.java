package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.vo.OneSubjectVo;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author jackson
 * @since 2021-08-05
 */
@Api(description = "課程分類管理")
@RestController
@RequestMapping("/eduservice/edusubject")

public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    @ApiOperation(value="批量導入課程分類")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        subjectService.addSubject(file,subjectService);
        return R.ok();
    }

    @ApiOperation(value="查詢所有課程分類")
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        List<OneSubjectVo> allSubjectList = subjectService.getAllSubject();
        return R.ok().data("allSubject",allSubjectList);
    }

}

