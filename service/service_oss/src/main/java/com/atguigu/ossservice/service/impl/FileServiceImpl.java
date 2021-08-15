package com.atguigu.ossservice.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.baseservice.handler.GlobalExceptionHandler;
import com.atguigu.commonutils.R;
import com.atguigu.ossservice.service.FileService;
import com.atguigu.ossservice.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFileOSS(MultipartFile file) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = ConstantPropertiesUtil.END_POINT;
// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        String fileName = file.getOriginalFilename();

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            //文件上傳
            InputStream inputStream = file.getInputStream();
            //優化文件名
            fileName = UUID.randomUUID().toString()+fileName;
            //優化文件存儲路徑(2021/03/09/uuid+1.jpg)
            String path = new DateTime().toString("yyyy/MM/dd");
            fileName = path+"/"+fileName;
            // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucketName, fileName, inputStream);
            ossClient.shutdown();
            //https://gulimail-j.oss-ap-northeast-1.aliyuncs.com/1.jpeg
            String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;

        } catch (IOException e) {
            e.printStackTrace();
            return R.error().getMessage();
        }

    }
}
