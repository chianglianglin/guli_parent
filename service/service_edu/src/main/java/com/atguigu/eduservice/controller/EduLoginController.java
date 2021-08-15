package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(description = "模擬登入")
@RestController
@RequestMapping("/eduuser")

public class EduLoginController {

    @ApiOperation(value="登陸")
    @PostMapping("login")
    //{"code":20000,"data":{"token":"admin"}}
    public R Login(){
        return R.ok().data("token","admin");
    }
    @ApiOperation(value="獲取用戶信息")
    @GetMapping("info")
    //{"code":20000,"data":{"roles":["admin"],"name":"admin","avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"}}
    public R info(){
        return R.ok().data("roles","admin")
                .data("name","admin")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
