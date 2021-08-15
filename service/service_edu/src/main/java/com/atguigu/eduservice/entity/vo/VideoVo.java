package com.atguigu.eduservice.entity.vo;

import lombok.Data;

@Data
public class VideoVo {

    private String id;

    private String title;
//我忘記要加上這個了 老師沒有講到 所以在前台獲取在pages/_vid.vue想要獲取video.videoSourceId時
//    找不到他
    private String videoSourceId;

}
