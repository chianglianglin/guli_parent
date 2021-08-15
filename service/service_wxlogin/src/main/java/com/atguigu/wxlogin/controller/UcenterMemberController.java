package com.atguigu.wxlogin.controller;


import com.atguigu.baseservice.handler.GuliException;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.wxlogin.entity.UcenterMember;
import com.atguigu.wxlogin.service.UcenterMemberService;
import com.atguigu.wxlogin.utils.ConstantPropertiesUtil;
import com.atguigu.wxlogin.utils.HttpClientUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.oracle.xmlns.internal.webservices.jaxws_databinding.JavaWsdlMappingType;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-03-19
 */
@Api(description="微信登录")
@Controller
@RequestMapping("/api/ucenter/wx")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    @GetMapping("login")
    public String linelogin(){
        //方式1   https://open.weixin.qq.com/connect/qrconnect?
        // appid=APPID&redirect_uri=REDIRECT_URI&
        // response_type=code&scope=SCOPE&state=STATE#wechat_redirect
        //方式2
//        line授權baseUrl https://access.line.me/dialog/oauth/weblogin
        // 微信开放平台授权baseUrl
//        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect"
//                "?appid=%s" +
//                "&redirect_uri=%s" +
//                "&response_type=code" +
//                "&scope=snsapi_login" +
//                "&state=%s" +
//                "#wechat_redirect";
        String baseUrl = "https://access.line.me/oauth2/v2.1/authorize" +
                "?response_type=code" +
                "&client_id=%s" +
                "&redirect_uri=%s" +
                "&state=%s" +
                //******要在%20之前加上%為了之後的String.format()不要認為%是要復植*****
                // 這個花了好久的時間才暸解 要加上%20試看line login api文檔說要加上空格
                //而%20是代表空格在url-encode
                "&scope=profile%%20" +
                "openid";


        // 回调地址

        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
            System.out.println(redirectUrl);
//            輸出結果http%3A%2F%2Fguli.shop%2Fapi%2Fucenter%2Fwx%2Fcallback
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }
//        String state = "imhelen";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
//        System.out.println("state = " + state);
//這是下面string.format的結果https://access.line.me/oauth2/v2.1/authorize?response_type=code&client_id=1656312036
// &redirect_uri=http%3A%2F%2Fguli.shop%2Fapi%2Fucenter%2Fwx%2Fcallback
// &state=atguigu&scope=profile%20openid
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu");
        System.out.println(qrcodeUrl);

        return "redirect:" + qrcodeUrl;
//確認地址 http://www.a.zgbook.top/api/ucenter/wx/callback?code=001QgBFa1wYYyB0GMpIa13zw8A2QgBFh&state=atguiguwxlogin
//#https://open.weixin.qq.com/connect/qrconnect?appid=wx4a81494979a58162&redirect_uri=http%3A%2F%2Fwww.a.zgbook.top%2F&response_type=code&scope=snsapi_login&state=3d6be0a4035d839573b04816624a415e#wechat_redirect
//https://open.weixin.qq.com/connect/qrconnect?appid=wxed9954c01bb89b47&redirect_uri=http%3A%2F%2Fguli.shop%2Fapi%2Fucenter%2Fwx%2Fcallback&response_type=code&scope=snsapi_login&state=atguiguwxlogin#wechat_redirect
    }
//確認地址 http://www.a.zgbook.top/api/ucenter/wx/callback?code=001QgBFa1wYYyB0GMpIa13zw8A2QgBFh&state=atguiguwxlogin

    @GetMapping("callback")
    public String callback(String code, String state){
        //1获取参数code，临时票据
        //無法成功因為回調位址為http://www.a.zgbook.top不是localhost:8150
        System.out.println("code="+code);
        System.out.println("state="+state);


        //2拿code，换取access_token、openid

//        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
//                "?appid=%s" +
//                "&secret=%s" +
//                "&code=%s" +
//                "&grant_type=authorization_code";
        // https://access.line.me/oauth2/v2.1/authorize  "&client_id=%s"
        String baseAccessTokenUrl = "https://api.line.me/oauth2/v2.1/token";
                //"?Content-Type=application/x-www-form-urlencoded"+
//                "&grant_type=authorization_code"+
//                "&code=%s" +
//                "&redirect_uri=%s" +
//                "&client_id=%s" +
//                "&client_secret=%s";
        //String body = "{'grant_type=authorization_code','code=%s','redirect_uri=%s','client_id=%s','client_secret=%s'}";
        String tokenbody = "grant_type=authorization_code&code=%s&redirect_uri=%s&client_id=%s&client_secret=%s";

        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }
//        Map<String, String> params = new HashMap<>();
//        params.put("grant_type","authorization_code");
//        params.put("code",code);
//        params.put("redirect_uri",redirectUrl);
//        params.put("client_id",ConstantPropertiesUtil.WX_OPEN_APP_ID);
//        params.put("client_secret",ConstantPropertiesUtil.WX_OPEN_APP_SECRET);
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type","application/x-www-form-urlencoded");

//        String accessTokenUrl = String.format(baseAccessTokenUrl,
//                code,
//                redirectUrl,
//                ConstantPropertiesUtil.WX_OPEN_APP_ID,
//                ConstantPropertiesUtil.WX_OPEN_APP_SECRET
//
//                );

        String result = null;
        String access_token = String.format(tokenbody,code,
                redirectUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET);
        System.out.println(access_token);
        try {
            result =  HttpClientUtils.post(baseAccessTokenUrl,access_token,"application/x-www-form-urlencoded","utf-8",5000,5000);
            System.out.println("result="+result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //2.1解析json
//        Gson gson = new Gson();
//        HashMap map = gson.fromJson(result, HashMap.class);
//        String accessToken = (String)map.get("access_token");
//        String openid = (String)map.get("openid");
        Gson gson = new Gson();
        HashMap map = gson.fromJson(result, HashMap.class);
        String id_token = (String)map.get("id_token");
        String verifybody = "id_token=%s&client_id=%s";
        String verify = String.format(verifybody,id_token,
                ConstantPropertiesUtil.WX_OPEN_APP_ID
                );
        System.out.println(verify);
//        String openid = (String)map.get("openid"); "sub": Ufedad950e9c767ca4cbf849ea0093c72
//
//        //3 换取用户信息
//        //访问微信的资源服务器，获取用户信息
//        String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
//        "?access_token=%s" +
//        "&openid=%s";
//        String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
//        String resultUserInfo = null;
//        try {
//            resultUserInfo =  HttpClientUtils.get(userInfoUrl);
//            System.out.println("resultUserInfo="+resultUserInfo);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String baseUserInfoUrl = "https://api.line.me/oauth2/v2.1/verify";
                //"?id_token=%s";

        //String userInfoUrl = String.format(baseUserInfoUrl,id_token);
        String resultUserInfo = null;
        try {
            resultUserInfo =  HttpClientUtils.post(baseUserInfoUrl,verify,"application/x-www-form-urlencoded","utf-8",5000,5000);
            System.out.println("resultUserInfo="+resultUserInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        //3.1解析json
//        HashMap userMap = gson.fromJson(resultUserInfo, HashMap.class);
//        String nickname = (String)userMap.get("nickname");
//        String headimgurl = (String)userMap.get("headimgurl");
        HashMap userMap = gson.fromJson(resultUserInfo, HashMap.class);
        String nickname = (String)userMap.get("name");
        String headimgurl = (String)userMap.get("picture");
        String openid = (String)userMap.get("sub");
//
//        //4根据openid查询用户
//        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("openid",openid);
//        UcenterMember member = memberService.getOne(queryWrapper);
//        //5判断用户是否存在，用户不存在，走注册
//        if(member==null){
//            member = new UcenterMember();
//            member.setNickname(nickname);
//            member.setAvatar(headimgurl);
//            member.setOpenid(openid);
//            memberService.save(member);
//        }
//        String token = JwtUtils.getJwtToken(member.getId(),member.getNickname());
//
//        return "redirect:http://localhost:3000?token="+token;
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        UcenterMember member = memberService.getOne(queryWrapper);
        //5判断用户是否存在，用户不存在，走注册
        if(member==null){
            member = new UcenterMember();
            member.setNickname(nickname);
            member.setAvatar(headimgurl);
            member.setOpenid(openid);
            memberService.save(member);
        }
        String token = JwtUtils.getJwtToken(member.getId(),member.getNickname());

        return "redirect:http://localhost:3000?token="+token;

    }








}

