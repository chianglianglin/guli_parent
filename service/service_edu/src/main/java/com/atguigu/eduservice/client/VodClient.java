package com.atguigu.eduservice.client;


import com.atguigu.commonutils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)
public interface VodClient {

    //"刪除視頻"
    //請求url必須完整
    //參數註解參數名不能省略
    @DeleteMapping(value="/eduvod/video/delVideo/{videoId}")
    public R delVideo(@PathVariable("videoId") String videoId);
    //"批量刪除視頻"
    @DeleteMapping("/eduvod/video/delVideoList")
    public R delVideList(@RequestParam("videoIdList") List<String> videoIdList);
}
