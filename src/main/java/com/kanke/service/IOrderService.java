package com.kanke.service;

import com.github.pagehelper.PageInfo;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Seat;
import com.kanke.vo.OrderVo;

import java.util.List;
import java.util.Map;

public interface IOrderService {

    ServerResponse creat(Integer scheduleId, List<Seat> seatList, Integer userId);

    ServerResponse<String> cancle(Integer userId,Long orderNo);

    ServerResponse getDetail (Long orderNo,Integer userId);

    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum , int pageSize);

    ServerResponse<PageInfo> OrderListManage(int pageNum ,int pageSize);

    ServerResponse<OrderVo> getDetailManage (Long orderNo);

    ServerResponse<PageInfo> searchOrder(Long orderNo,int pageNum,int pageSize);

    ServerResponse pay(Long orderNo, Integer userId , String path);

    ServerResponse alipayCallback(Map<String,String> params);

    ServerResponse queryOrderPayStatus(Long orderNo,Integer userId);
}
