package com.atguigu.ossservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.ossservice.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "文件管理")
@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation(value="上傳文件")
    @PostMapping("uploadFile")
    public R uploadFile(MultipartFile file){
        //1.獲取文件

        //2.調用街口上傳文件,獲取url
        String url = fileService.uploadFileOSS(file);
        return R.ok().data("url",url);
    }

}
