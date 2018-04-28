package com.kanke.service;

import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Seat;

import java.util.List;

public interface ISeatService {
    ServerResponse<List<Seat>> getSeatDetail(Integer hallId);

    ServerResponse<Seat> selectSeat(Integer seatId);

    ServerResponse updateSeatStatus(Integer seatId,Integer status);
}
