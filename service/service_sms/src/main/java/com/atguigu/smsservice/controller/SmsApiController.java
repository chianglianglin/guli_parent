package com.atguigu.smsservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.smsservice.service.SmsService;
import com.atguigu.smsservice.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Api(description="短信发送")
@RestController
@RequestMapping("/edusms/sms")
@CrossOrigin
public class SmsApiController {

    @Autowired
    private SmsService smsService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @ApiOperation(value = "短信發送")
    @GetMapping("/send/{phone}")
    public R sendSmsPhone(@PathVariable String phone){
        //1拿手機號到redis查詢是否有驗證碼
        String rPhone = redisTemplate.opsForValue().get(phone);

        //2驗證驗證碼是否存在,直接返回ok
        if(rPhone!=null){
            return R.ok();
        }
        //3驗證碼不存在,生成驗證碼
        String code = RandomUtil.getFourBitRandom();
        //4調用接口服務發送短信
//        Map<String,String>map = new HashMap<>();
//        map.
        boolean isSend = smsService.sendSmsPhone(phone,code);
        //5發送成功驗證碼存入redis,時效5分鐘
        if (isSend){
            redisTemplate.opsForValue().set(phone,code,60, TimeUnit.MINUTES);
            return R.ok();
        }else{
            return R.error();
        }

    }

}
