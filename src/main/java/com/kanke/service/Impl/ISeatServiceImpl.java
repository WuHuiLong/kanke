package com.kanke.service.Impl;

import com.kanke.commom.Const;
import com.kanke.commom.ServerResponse;
import com.kanke.dao.HallMapper;
import com.kanke.dao.SeatMapper;
import com.kanke.pojo.Hall;
import com.kanke.pojo.Seat;
import com.kanke.service.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iSeatService")
public class ISeatServiceImpl implements ISeatService {
    @Autowired
    private SeatMapper seatMapper;
    @Autowired
    private HallMapper hallMapper;

    public ServerResponse<Seat> getSeatDetail(Seat seat,Integer hallId){
        if(seat!=null&&hallId!=null){

        }
        Hall hall=hallMapper.selectByPrimaryKey(hallId);
        if(hall==null){
            return ServerResponse.createByErrorMsg("没有该放映厅或该放映厅未开放");
        }
        if(hall.getStatus()!= Const.HallStatusEnum.IDLE.getCode()){
            return ServerResponse.createByErrorMsg("没有该放映厅或该放映厅未开放");
        }
        return null;
    }

}
