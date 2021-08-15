package com.atguigu.eduservice.excel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExcelTest {
    @Test
    public void writeTest(){
        String fileName = "/Users/jackson/Documents/excel01/write.xlsx";
        EasyExcel.write(fileName, DemoData.class).sheet("学生列表").doWrite(data());
    }
    @Test
    public void  readTest(){
        String fileName = "/Users/jackson/Documents/excel01/write.xlsx";
        EasyExcel.read(fileName, DemoData.class,new ExcelListener()).sheet().doRead();
    }


    //循环设置要添加的数据，最终封装到list集合中
    private static List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("张三"+i);
            list.add(data);
        }
        return list;
    }

}
