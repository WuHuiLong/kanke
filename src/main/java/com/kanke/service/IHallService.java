package com.kanke.service;

import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Hall;

public interface IHallService {
    ServerResponse addHall(Hall hall);

    ServerResponse updateHall(Hall hall);

    ServerResponse<String> deleteHall(Integer hallId);

    ServerResponse<Hall> selectHall(Integer hallId);

    ServerResponse<String> setHallStatus(Integer hallId ,Integer status);

}
