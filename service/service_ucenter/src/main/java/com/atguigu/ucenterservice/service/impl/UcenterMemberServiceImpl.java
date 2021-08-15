package com.atguigu.ucenterservice.service.impl;

import com.atguigu.baseservice.handler.GuliException;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.atguigu.ucenterservice.utils.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author jackson
 * @since 2021-08-11
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //用戶註冊
    @Override
    public void register(RegisterVo registerVo) {
        //1.獲取數據.驗空
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String code = registerVo.getCode();
        String password = registerVo.getPassword();
        if(StringUtils.isEmpty(nickname)||StringUtils.isEmpty(mobile)||
                StringUtils.isEmpty(code)||StringUtils.isEmpty(password)){
            throw new GuliException(20001,"註冊信息缺失");
        }
        //2.驗證手機號是否重複
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count>0){
            throw new GuliException(20001,"手機號重複");
        }
        //3.驗證短信驗證碼
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)){
            throw new GuliException(20001,"驗證碼錯誤");
        }
        //4.使用MD5加密密碼
        String md5Password = MD5.encrypt(password);
        //5.補充信息插入數據庫
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setNickname(nickname);
        ucenterMember.setMobile(mobile);
        ucenterMember.setPassword(md5Password);
        ucenterMember.setAvatar("https://gulimail-j.oss-ap-northeast-1.aliyuncs.com/2021/08/05/e249d49a-fdb8-40b0-9055-5c8327e47ad8file.png");
        ucenterMember.setIsDisabled(false);
        baseMapper.insert(ucenterMember);

    }

    //用戶登入
    @Override
    public String login(LoginVo loginVo) {
        //獲取參數,驗空
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            throw new GuliException(20001,"手機號或密碼有誤");
        }
        //根據手機號獲取用戶信息
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(queryWrapper);
        if (ucenterMember==null){
            throw new GuliException(20001,"手機號或密碼有誤");
        }
        //取出密碼加密後,驗證密碼
        String md5Password = MD5.encrypt(password);
        if(!md5Password.equals(ucenterMember.getPassword())){
            throw new GuliException(20001,"手機號或密碼有誤");
        }
        //生成token字符串
        String token = JwtUtils.getJwtToken(ucenterMember.getId(),ucenterMember.getNickname());

        return token;
    }

    //統計註冊人數遠程調用
    @Override
    public Integer countRegister(String day) {
        Integer count = baseMapper.countRegister(day);
        return count;
    }
}
