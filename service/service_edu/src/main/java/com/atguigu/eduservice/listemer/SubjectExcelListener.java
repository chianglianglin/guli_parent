package com.atguigu.eduservice.listemer;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.baseservice.handler.GuliException;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.ExcelSubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {
    public EduSubjectService subjectService;

    public SubjectExcelListener(){}
    public SubjectExcelListener(EduSubjectService subjectService){
        this.subjectService = subjectService;
    }

    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        //1.讀取數據驗空
        if(excelSubjectData==null){
            throw  new GuliException(20001,"导入课程分类失败");
        }
        //2判断一级分类名称是否重复
        EduSubject existOneSubject = this.existOneSubject(subjectService, excelSubjectData.getOneSubjectName());

        //3一级不重复插入数据库
        if(existOneSubject==null){
            //創建一個新的對象
            existOneSubject = new EduSubject();
            //再把數據放進去
            existOneSubject.setTitle(excelSubjectData.getOneSubjectName());
            //一級分類的parentId為0
            existOneSubject.setParentId("0");
            subjectService.save(existOneSubject);
        }
        //取出一級分類的id再來判斷二級
        String pid = existOneSubject.getId();
        //4判断二级名称是否重复
        EduSubject existTwoSubject = this.existTwoSubject(subjectService, excelSubjectData.getTwoSubjectName(), pid);
        //5二级不重复插入数据库
        if(existTwoSubject==null){
            existTwoSubject = new EduSubject();
            existTwoSubject.setTitle(excelSubjectData.getTwoSubjectName());
            existTwoSubject.setParentId(pid);
            subjectService.save(existTwoSubject);
        }
    }

    //判断一级分类是否重复
    private EduSubject existOneSubject(EduSubjectService subjectService, String name) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id","0");
        wrapper.eq("title",name);
        EduSubject eduSubject = subjectService.getOne(wrapper);
        return eduSubject;
    }

    //判断二级分类是否重复
    private EduSubject existTwoSubject(EduSubjectService subjectService, String name,String pid) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", pid);
        wrapper.eq("title", name);
        EduSubject eduSubject = subjectService.getOne(wrapper);
        return eduSubject;
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
