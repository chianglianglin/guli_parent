package com.atguigu.smsservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.smsservice.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService{
    //發送短信
    @Override
    public boolean sendSmsPhone(String phone, String code) {
        if(StringUtils.isNotEmpty(phone)){
            Twilio.init("ACa4192b73c8ee26c2bd47ab0aca33ac09","fc380c27045503b461abec8806e0e887");
//phone, new PhoneNumber("+16504801751"),ACa4192b73c8ee26c2bd47ab0aca33ac09
            //終於成功了 要把你要傳給的手機號碼 在twilio做認證 我是用美國的手機號在這個網站找到的
            // http://receive-sms-online.info/ 2021/08/11使用+14703105856這組手機號
            Message message = Message.creator(new PhoneNumber("+1"+phone),new PhoneNumber("+16504801751"),code).create();
            //Call call = Call.creator(new PhoneNumber(phone),new PhoneNumber("+16504801751"),code).create();
            System.out.println(message.getSid());
            //System.out.println(call.getSid());
            return true;
        }else{
            return false;
        }


    }
}
