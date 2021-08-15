package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author jackson
 * @since 2021-08-03
 */
public interface EduTeacherService extends IService<EduTeacher> {

    Map<String, Object> getTeacherApiPage(Page<EduTeacher> page);
}
