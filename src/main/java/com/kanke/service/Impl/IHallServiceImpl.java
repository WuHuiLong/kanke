package com.kanke.service.Impl;

import com.kanke.commom.ServerResponse;
import com.kanke.dao.HallMapper;
import com.kanke.pojo.Hall;
import com.kanke.service.IHallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iHallService")
public class IHallServiceImpl implements IHallService {
    @Autowired
    private HallMapper hallMapper;

    public ServerResponse addHall(Hall hall){
        if(hall==null){
            return ServerResponse.createByErrorMsg("参数错误，请重新再试");
        }
        int rowCount=hallMapper.insert(hall);
        if(rowCount>0){
            return ServerResponse.createBySuccess("添加影厅成功");
        }
        return ServerResponse.createByErrorMsg("添加影厅失败");
    }



}
