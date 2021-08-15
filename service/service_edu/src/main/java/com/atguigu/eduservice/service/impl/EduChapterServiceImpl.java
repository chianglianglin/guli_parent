package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author jackson
 * @since 2021-08-06
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;
    //根據課程id查詢章節,小節信息
    @Override
    public List<ChapterVo> getChapterVideoById(String courseId) {
        //根據courseId查詢章節集合
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_Id",courseId);
        List<EduChapter> chapterList= baseMapper.selectList(chapterWrapper);
        //根據courseId查詢小節集合
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_Id",courseId);
        List<EduVideo> videoList = eduVideoService.list(videoWrapper);
        //遍歷章節信息進行封裝
        List<ChapterVo> chapterVideoList = new ArrayList<>();
        for (int i = 0; i < chapterList.size(); i++) {
            EduChapter eduChapter = chapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            chapterVideoList.add(chapterVo);
            //遍歷和此章節關聯的小節信息進行封裝
            List<VideoVo> videoVos = new ArrayList<>();
            for (int m = 0; m < videoList.size(); m++) {
                EduVideo eduVideo = videoList.get(m);
                if(eduChapter.getId().equals(eduVideo.getChapterId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVos.add(videoVo);
                }
                chapterVo.setChildren(videoVos);
            }
        }
        
        return chapterVideoList;
    }
}
