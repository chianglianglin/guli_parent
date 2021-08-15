package com.atguigu.smsservice.service;

public interface SmsService {
    boolean sendSmsPhone(String phone, String code);
}
