package com.kanke.service;

import com.github.pagehelper.PageInfo;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Hall;

public interface IHallService {
    ServerResponse addHall(Hall hall);

    ServerResponse findAllKind();

    ServerResponse updateHall(Hall hall);

    ServerResponse<String> deleteHall(Integer hallId);

    ServerResponse<Hall> selectHall(Integer hallId);

    ServerResponse<String> setHallStatus(Integer hallId ,Integer status);

    ServerResponse<PageInfo> hallList(int pageNum, int pageSize);

}
