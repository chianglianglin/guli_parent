package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient{
    @Override
    public R delVideo(String videoId) {
        return R.error().message("刪除失敗");
    }

    @Override
    public R delVideList(List<String> videoIdList) {
        return R.error().message("刪除失敗");
    }
}
