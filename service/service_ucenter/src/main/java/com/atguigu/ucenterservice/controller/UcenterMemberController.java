package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.vo.UcenterMemberForOrder;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author jackson
 * @since 2021-08-11
 */
@Api(description = "前台用戶會員管理")
@RestController
@RequestMapping("/ucenterservice/member")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;


    @ApiOperation(value="用戶註冊")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }
    @ApiOperation(value = "用户登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){
        String token = memberService.login(loginVo);
        return R.ok().data("token",token);
    }

    @ApiOperation(value="根據token字符串獲取用戶信息")
    @GetMapping("getUcenterByToken")
    public R getUcenterByToken(HttpServletRequest request){
        String menberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember ucenterMember = memberService.getById(menberId);
        return R.ok().data("ucenterMember",ucenterMember);
    }

    @ApiOperation(value="根據memberId獲取用戶信息跨模塊")
    @GetMapping("getUcenterForOrder/{memberId}")
    public UcenterMemberForOrder getUcenterForOrder(@PathVariable("memberId") String memberId){
        UcenterMember ucenterMember = memberService.getById(memberId);
        UcenterMemberForOrder ucenterMemberForOrder =new UcenterMemberForOrder();
        BeanUtils.copyProperties(ucenterMember,ucenterMemberForOrder);
        return ucenterMemberForOrder;
    }
    @ApiOperation(value="統計註冊人數遠程調用")
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable("day") String day){
        Integer count = memberService.countRegister(day);
        return R.ok().data("countRegister",count);
    }


}

