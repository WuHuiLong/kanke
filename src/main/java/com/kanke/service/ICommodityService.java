package com.kanke.service;

import com.github.pagehelper.PageInfo;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Commodity;
import com.kanke.vo.CommodityDetailVo;

public interface ICommodityService {

    ServerResponse saveOrUpdateMovie(Commodity commodity);

    ServerResponse<String> setSaleStatus(Integer commodityId,Integer status);

    ServerResponse<CommodityDetailVo> manageCommodityDetail(Integer commodityId);

    ServerResponse<PageInfo> getCommodityList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchCommodity(String commodityName,Integer commodityId,int pageNum,int pageSize);

    ServerResponse<CommodityDetailVo> detail(Integer commodityId);

    ServerResponse<PageInfo> searchList(String keyword,int pageNum,int pageSize);

    ServerResponse<PageInfo> AllSelect(int pageNum,int pageSize);
}
