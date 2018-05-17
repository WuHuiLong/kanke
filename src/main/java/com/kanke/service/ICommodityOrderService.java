package com.kanke.service;

import com.github.pagehelper.PageInfo;
import com.kanke.commom.ServerResponse;
import com.kanke.vo.CommodityOrderVo;

import java.util.Map;

public interface ICommodityOrderService {
    ServerResponse creat(Integer userId);

    ServerResponse<String> cancle(Integer userId,Long orderNo);

    ServerResponse<CommodityOrderVo> getCommodityOrderDetail(Integer userId, Long orderNo);

    ServerResponse<PageInfo> getCommodityOrderList(Integer userId, int pageNum, int pageSize);

    ServerResponse<PageInfo> getCommodityOrderListManage( int pageNum, int pageSize);

    ServerResponse<CommodityOrderVo> getCommodityOrderDetailManage(Long orderNo);

    ServerResponse<PageInfo> searchOrder(Long orderNo,int pageNum,int pageSize);

    ServerResponse pay(Long orderNo, Integer userId , String path);

    ServerResponse alipayCallback(Map<String,String> params);

    ServerResponse queryOrderPayStatus(Long orderNo,Integer userId);
}
