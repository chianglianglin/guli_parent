package com.atguigu.eduservice.service.impl;

import com.atguigu.baseservice.handler.GuliException;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author jackson
 * @since 2021-08-06
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;
    //添加課程信息
    @Override
    public String addCourseInfo(CourseInfoForm courseInfoForm) {
        //1添加課程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert  == 0){
            throw new GuliException(20001,"創建課程失敗");
        }
        //2獲取課程id
        String courseId = eduCourse.getId();
        //3添加課程描述信息
        EduCourseDescription courseDescription = new EduCourseDescription();
        //用普通的方法就可以set get
        courseDescription.setId(courseId);
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescriptionService.save(courseDescription);
        return courseId;
    }

    //根據id查詢課程信息
    @Override
    public CourseInfoForm getCourseInfoById(String id) {

        //1.根據id查詢課程信息
        EduCourse eduCourse = baseMapper.selectById(id);
        //2.封裝課程信息
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(eduCourse,courseInfoForm);
        //3.根據id查詢課程描述信息
        EduCourseDescription courseDescription = courseDescriptionService.getById(id);

        //4.封裝課程描述
        courseInfoForm.setDescription(courseDescription.getDescription());
        return courseInfoForm;
    }

    @Override
    public void updateCourseInfo(CourseInfoForm courseInfoForm) {
        //1.複製課程數據
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm, eduCourse);
        //2.更新課程數據
        int i = baseMapper.updateById(eduCourse);

        //3.判斷是否成功
        if(i==0){
            throw new GuliException(20001,"修改課程失敗");
        }

        //4.更新課程描述
        EduCourseDescription CourseDescription = new EduCourseDescription();
        CourseDescription.setId(courseInfoForm.getId());
        CourseDescription.setDescription(courseInfoForm.getDescription());
        courseDescriptionService.updateById(CourseDescription);
    }

    //根據課程id查詢課程發布信息
    @Override
    public CoursePublishVo getCoursePublishById(String id) {
        CoursePublishVo coursePublishVo =
                baseMapper.getCoursePublishById(id);
        return coursePublishVo;
    }
    //根據id刪除課程相關信息
    public void delCourseInfo(String id){
        //1.刪除視頻
        QueryWrapper<EduVideo> videoIdWrapper = new QueryWrapper<>();
        videoIdWrapper.eq("course_id",id);
        List<EduVideo> list = videoService.list(videoIdWrapper);
        //1.2遍歷獲取視頻id
        List<String> videoIdList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            EduVideo eduVideo = list.get(i);
            videoIdList.add(eduVideo.getVideoSourceId());

        }
        //1.3判斷,調接口
        if(videoIdList.size()>0){
            vodClient.delVideList(videoIdList);
        }
        //2.刪除小節
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id",id);
        videoService.remove(videoWrapper);
        //3.刪除章節
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id",id);
        chapterService.remove(chapterWrapper);
        //4.刪除課程描述
        courseDescriptionService.removeById(id);
        //5.刪除課程
        int delete = baseMapper.deleteById(id);
        if(delete==0){
            throw new GuliException(20001,"刪除課程失敗");
        }
    }

    //帶條件分頁查詢課程列表
    @Override
    public Map<String, Object> getCourseApiPageVo(Page<EduCourse> pageParam, CourseQueryVo courseQueryVo) {
       //1.取出查詢條件
        String subjectParentId = courseQueryVo.getSubjectParentId();
        String subjectId = courseQueryVo.getSubjectId();
        String buyCountSort = courseQueryVo.getBuyCountSort();
        String gmtCreateSort = courseQueryVo.getGmtCreateSort();
        String priceSort = courseQueryVo.getPriceSort();

        //2.驗空
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("subject_parent_id",subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("subject_id",subjectId);
        }
        if (!StringUtils.isEmpty(buyCountSort)){
            queryWrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(gmtCreateSort)){
            queryWrapper.orderByDesc("gmt_create");
        }
        if (!StringUtils.isEmpty(priceSort)){
            queryWrapper.orderByDesc("price");
        }
        //3.分頁查詢
        baseMapper.selectPage(pageParam,queryWrapper);

        //4.封裝
        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;

    }

    //根據課程id課程相關信息
    @Override
    public CourseWebVo getCourseWebVo(String id) {
        CourseWebVo courseWebVo = baseMapper.getCourseWebVo(id);
        return courseWebVo;
    }
}
