package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.service.TOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author jackson
 * @since 2021-08-14
 */
@Api(description="創建訂單")
@RestController
@RequestMapping("/orderservice/order")
@CrossOrigin
public class TOrderController {

    @Autowired
    private TOrderService orderService;


    @ApiOperation(value = "根据课程id、用户id创建订单")
    @PostMapping("createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId,
                         HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        String orderNo = orderService.createOrder(courseId,memberId);
        return R.ok().data("orderNo",orderNo);
    }

    @ApiOperation(value="根據訂單編號查詢訂單信息")
    @GetMapping("getOrderInfo/{orderNo}")
    public R getOrderInfo(@PathVariable String orderNo){
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        TOrder order = orderService.getOne(queryWrapper);
        return R.ok().data("order",order);
    }

}


