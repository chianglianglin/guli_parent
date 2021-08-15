package com.atguigu.wxlogin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.atguigu"})
@SpringBootApplication
@MapperScan("com.atguigu.wxlogin.mapper")
public class WxloginApplication {
    public static void main(String[] args) {
        SpringApplication.run(WxloginApplication.class, args);
    }
}

