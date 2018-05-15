package com.kanke.service.Impl;

import com.google.common.collect.Lists;
import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.dao.HallMapper;
import com.kanke.dao.SeatMapper;
import com.kanke.pojo.Hall;
import com.kanke.pojo.Seat;
import com.kanke.service.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iSeatService")
public class ISeatServiceImpl implements ISeatService {
    @Autowired
    private SeatMapper seatMapper;
    @Autowired
    private HallMapper hallMapper;

    public ServerResponse<List<Seat>> getSeatDetail(Integer hallId){
        if(hallId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Hall hall=hallMapper.selectByPrimaryKey(hallId);
        if(hall==null){
            return ServerResponse.createByErrorMsg("没有该放映厅或该放映厅未开放");
        }
        if(hall.getStatus()!= Const.HallStatusEnum.IDLE.getCode()){
            return ServerResponse.createByErrorMsg("没有该放映厅或该放映厅未开放");
        }
//      List<Seat> seatListItem=Lists.newArrayList();
        List<Seat> seatList=seatMapper.selectList(hallId);
        //前端处理就好了，后台只要返回数据就行了
//        for(Seat seatItem :seatList){
//            if(seatItem.getStatus()==Const.SeatStatusEnum.UN_SELECTABLE.getCode()){
//                return ServerResponse.createBySuccess("不可选座位",seatItem);
//            }
//            if(seatItem.getStatus()==Const.SeatStatusEnum.SELECTABLE.getCode()){
//                return ServerResponse.createBySuccess("可选座位",seatItem);
//            }
//            if(seatItem.getStatus()==Const.SeatStatusEnum.REVERSIBILITY.getCode()){
//                return ServerResponse.createBySuccess("可选座位",seatItem);
//            }
//        }
        return ServerResponse.createBySuccess(seatList);
    }

    public ServerResponse<Seat> selectSeat(Integer seatId){//选择一个座位
        if(seatId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Seat seat =seatMapper.selectByPrimaryKey(seatId);
        if(seat==null){
            return ServerResponse.createByErrorMsg("您点错了，这没座位哦");
        }
//        if(seat.getStatus()==Const.SeatStatusEnum.UN_SELECTABLE.getCode()){
//            return ServerResponse.createByErrorMsg("亲，你来迟了，这个座位已经被选了");
//        }
//        else if(seat.getStatus()==Const.SeatStatusEnum.SELECTABLE.getCode()){
//            return ServerResponse.createBySuccess(seat.getRow()+"排"+seat.getColumn()+"列");
//        }
//        else if(seat.getStatus()==Const.SeatStatusEnum.REVERSIBILITY.getCode()){
//            return ServerResponse.createBySuccess("已撤销");
//        }
        return ServerResponse.createBySuccess(seat);
    }

    public ServerResponse updateSeatStatus(Integer seatId,Integer status){//改变座位状态
        Seat seatitem =seatMapper.selectByPrimaryKey(seatId);
        if(seatitem==null){
            return ServerResponse.createByErrorMsg("没有这个座位哦");
        }
        Seat seat = new Seat();
        seat.setId(seatId);
        if(seatitem.getStatus() == Const.SeatStatusEnum.UN_SELECTABLE.getCode()){
            return ServerResponse.createByError();
        }
        else{
            seat.setStatus(status);
        }
        int rowCount=seatMapper.updateByPrimaryKeySelective(seat);
        if(rowCount>0){
            return ServerResponse.createBySuccessMsg("更改状态成功");
        }
        return  ServerResponse.createByErrorMsg("更改状态失败");
    }

    //显示所有已经选择的座位
    public ServerResponse<List<Seat>> getSeatSpecial(Integer hallId){
        if(hallId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Seat> seatList =seatMapper.selectSpecial(hallId);
        return ServerResponse.createBySuccess(seatList);
    }

}
