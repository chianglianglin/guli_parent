package com.atguigu.vodservice.controller;


import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.baseservice.handler.GuliException;
import com.atguigu.commonutils.R;
import com.atguigu.vodservice.utils.AliyunVodSDKUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Api(description = "視頻管理")
@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VideoAdminController {

    @ApiOperation(value="上傳視頻")
    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String title = originalFilename.substring(0,originalFilename.lastIndexOf("."));
            UploadStreamRequest request = new UploadStreamRequest(
                     "LTAI5t61qb242ZRp322RbDhK",
                     "qTmkuaBLq5hNfop2EPijnDtj9GrjpI",
                     title,
                     originalFilename,
                     inputStream
            );
            //＊＊＊＊＊注意注意要加上地區id喔＊＊＊＊＊
            request.setApiRegionId("ap-northeast-1");

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            String videoId = response.getVideoId();
            return R.ok().data("videoId",videoId);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001,"上傳視頻失敗");
        }
    }

    @ApiOperation(value="刪除視頻")
    @DeleteMapping("delVideo/{videoId}")
    public R delVideo(@PathVariable("videoId") String videoId){

        try {
            //初始化客戶端對象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient("LTAI5t61qb242ZRp322RbDhK",
                    "qTmkuaBLq5hNfop2EPijnDtj9GrjpI");
            //創建請求對象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //創建響應對象
            //DeleteVideoResponse response = new DeleteVideoResponse();
            //支持傳入多個視頻,多個用逗號分割
            request.setVideoIds(videoId);
            //調用客戶端對象方法發送請求,拿到響應
            client.getAcsResponse(request);
            return R.ok();

        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001,"刪除視頻失敗");
        }
    }
    @ApiOperation(value="批量刪除視頻")
    @DeleteMapping("delVideoList")
    public R delVideoList(@RequestParam("videoIdList") List<String> videoIdList){
        try {
            //初始化客戶端對象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient("LTAI5t61qb242ZRp322RbDhK",
                    "qTmkuaBLq5hNfop2EPijnDtj9GrjpI");
            //創建請求對象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //創建響應對象
            //DeleteVideoResponse response = new DeleteVideoResponse();
            //支持傳入多個視頻,多個用逗號分割
            String videoIds = StringUtils.join(videoIdList.toArray(),",");
            request.setVideoIds(videoIds);
            //調用客戶端對象方法發送請求,拿到響應
            client.getAcsResponse(request);
            return R.ok();

        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001,"批量刪除視頻失敗");
        }
    }
    @ApiOperation(value = "根据视频id获取视频播放凭证")
    @GetMapping("getPlayAuth/{vid}")
    public R getPlayAuth(@PathVariable String vid){
        try {
            //（1）创建初始化对象
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient("LTAI5t61qb242ZRp322RbDhK", "qTmkuaBLq5hNfop2EPijnDtj9GrjpI");
            //（2）创建request、response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
            //（3）向request设置视频id
            request.setVideoId(vid);
            //播放凭证有过期时间，默认值：100秒 。取值范围：100~3000。
            request.setAuthInfoTimeout(200L);
            //（4）调用初始化方法实现功能
            response = client.getAcsResponse(request);
            //（5）调用方法返回response对象，获取内容
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        } catch (ClientException e) {
            throw new GuliException(20001,"获取视频凭证失败");
        }
    }
}
