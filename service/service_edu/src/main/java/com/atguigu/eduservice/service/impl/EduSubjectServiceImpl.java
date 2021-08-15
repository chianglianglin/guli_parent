package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.baseservice.handler.GuliException;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.ExcelSubjectData;
import com.atguigu.eduservice.entity.vo.OneSubjectVo;
import com.atguigu.eduservice.entity.vo.TwoSubjectVo;
import com.atguigu.eduservice.listemer.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author jackson
 * @since 2021-08-05
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //批量導入課程分類
    @Override
    public void addSubject(MultipartFile file, EduSubjectService subjectService) {
        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, ExcelSubjectData.class,
                    new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001,"導入課程分類失敗");
        }
    }

    //查詢所有課程分類
    @Override
    public List<OneSubjectVo> getAllSubject() {

        //1.查詢所有一級分類
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);
        //2.查詢所有二級分類
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);
        //3.封裝一級分類
        List<OneSubjectVo> allSubjectList = new ArrayList<>();
        for(EduSubject oneSubject:oneSubjectList){
            //3.1創建一級的Vo
            OneSubjectVo oneSubjectVo = new OneSubjectVo();
            //這個太麻煩了要一直set get很多字段就麻煩推薦下面的方法
//            oneSubjectVo.setId(oneSubject.getId());
//            oneSubjectVo.setTitle(oneSubject.getTitle());
            //3.2把EduSubject轉化為OneSubjectVo
            BeanUtils.copyProperties(oneSubject,oneSubjectVo);
            allSubjectList.add(oneSubjectVo);
            //4.找到跟一級有關的二級進行封裝
            List<TwoSubjectVo> twoSubjectVos  = new ArrayList<>();
            for(EduSubject twoSubject:twoSubjectList){
                TwoSubjectVo twoSubjectVo = new TwoSubjectVo();
                //4.1判斷二級的parentId是否歸屬於一級  字符串要用equals
                if(twoSubject.getParentId().equals(oneSubject.getId())){
                    //4.2是的話 把EduSubject轉化為TwoSubjectVo
                    BeanUtils.copyProperties(twoSubject,twoSubjectVo);
                    twoSubjectVos.add(twoSubjectVo);
                }
            }
            oneSubjectVo.setChildren(twoSubjectVos);

        }
        //4.找到跟一級有關的二級進行封裝
        return allSubjectList;
    }
}
