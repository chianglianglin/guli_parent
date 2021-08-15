package com.atguigu.orderservice.client;

import com.atguigu.commonutils.vo.UcenterMemberForOrder;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    //根據memberId獲取用戶信息跨模塊
    @GetMapping("/ucenterservice/member/getUcenterForOrder/{memberId}")
    public UcenterMemberForOrder getUcenterForOrder(@PathVariable("memberId") String memberId);
}
