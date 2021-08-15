package com.atguigu.orderservice.service.impl;

import com.atguigu.baseservice.handler.GuliException;
import com.atguigu.commonutils.vo.CourseWebVoForOrder;
import com.atguigu.commonutils.vo.UcenterMemberForOrder;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.mapper.TOrderMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author jackson
 * @since 2021-08-14
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public String createOrder(String courseId, String memberId) {
        //1.跨模塊獲取課程信息
        CourseWebVoForOrder courseInfoForOrder = eduClient.getCourseInfoForOrder(courseId);
        //1.1進行校驗
        if(courseInfoForOrder==null){
            new GuliException(20001,"獲取課程信息失敗");
        }
        //2.跨模塊獲取用戶信息
        UcenterMemberForOrder ucenterForOrder = ucenterClient.getUcenterForOrder(memberId);
        //2.1進行校驗
        if (ucenterForOrder==null){
            new GuliException(20001,"獲取用戶信息失敗");
        }
        //3.生成訂單編號
        String orderNo = OrderNoUtil.getOrderNo();
        //4.封裝數據.存入數據
        TOrder order = new TOrder();
        order.setOrderNo(orderNo);
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoForOrder.getTitle());
        order.setCourseCover(courseInfoForOrder.getCover());
        order.setTeacherName(courseInfoForOrder.getTeacherName());
        order.setTotalFee(courseInfoForOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterForOrder.getMobile());
        order.setNickname(ucenterForOrder.getNickname());
        order.setStatus(0);//未支付
        order.setPayType(1);//1：微信
        baseMapper.insert(order);

        return orderNo;

    }
}
