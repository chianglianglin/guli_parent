package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author jackson
 * @since 2021-08-03
 */
@Api(description = "講師管理")
@RestController
@RequestMapping("/eduservice/eduteacher")
//@CrossOrigin
public class EduTeacherController {
    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation(value = "所有講師列表")
    @GetMapping
    public R getAllTeacher() {
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("list", list);
    }

    @DeleteMapping("{id}")
    public R delTeacher(@PathVariable String id) {
        boolean remove = eduTeacherService.removeById(id);
        if (remove) {
            return R.ok();
        } else {
            return R.error();
        }

    }

    @ApiOperation(value = "分頁查詢講師列表")
    @GetMapping("getTeacherPage/{current}/{limit}")
    public R getTeacherPage(@PathVariable Long current,
                            @PathVariable Long limit) {
        Page<EduTeacher> page = new Page<>(current, limit);
        eduTeacherService.page(page, null);
        List<EduTeacher> records = page.getRecords();
        long total = page.getTotal();
        //1.Map
//        Map<String,Object> map = new HashMap<>();
//        map.put("list",records);
//        map.put("total",total);
//        return R.ok().data(map);
        //2.直接拼接
        return R.ok().data("list", records).data("total", total);
    }

    @ApiOperation(value = "帶條件分頁查詢講師列表")
    @PostMapping("getTeacherPageVo/{current}/{limit}")
    public R getTeacherPage(@PathVariable Long current,
                            @PathVariable Long limit,
                            @RequestBody TeacherQuery teacherQuery) {
        //@RequestBody把json串轉化成實體類
        //1.取出查詢條件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //2.判斷條件是否為空,如不為空拼接寫sql
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!ObjectUtils.isEmpty(level)) {
            wrapper.like("level", level);
        }
        if (!ObjectUtils.isEmpty(begin)) {
            wrapper.like("gmt_create", begin);
        }
        if (!ObjectUtils.isEmpty(end)) {
            wrapper.like("gmt_create", end);
        }

        Page<EduTeacher> page = new Page<>(current, limit);
        eduTeacherService.page(page, wrapper);
        List<EduTeacher> records = page.getRecords();
        long total = page.getTotal();
        return R.ok().data("list", records).data("total", total);
    }
    @ApiOperation(value = "添加講師")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }else{
            return R.error();
        }
    }
    @ApiOperation(value = "根據id查詢講師列表")
    @GetMapping("getTeacherById/{id}")
    public R getTeacherById(@PathVariable String id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("eduTeacher",eduTeacher);
    }

    @ApiOperation(value = "修改講師")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean update = eduTeacherService.updateById(eduTeacher);
        if(update){
            return R.ok();
        }else{
            return R.error();
        }

    }
}

