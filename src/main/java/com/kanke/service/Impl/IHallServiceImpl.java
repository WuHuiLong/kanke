package com.kanke.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.kanke.commom.Const;
import com.kanke.commom.ServerResponse;
import com.kanke.dao.HallMapper;
import com.kanke.dao.KindMapper;
import com.kanke.dao.SeatMapper;
import com.kanke.pojo.Hall;
import com.kanke.pojo.Kind;
import com.kanke.pojo.Seat;
import com.kanke.service.IHallService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("iHallService")
public class IHallServiceImpl implements IHallService {
    @Autowired
    private HallMapper hallMapper;
    @Autowired
    private SeatMapper seatMapper;
    @Autowired
    private KindMapper kindMapper;

    public ServerResponse addHall(Hall hall){
        if(hall==null){
            return ServerResponse.createByErrorMsg("参数错误，请重新再试");
        }
        List<Kind> kindList =kindMapper.selectByStype(hall.getStype());
        List<Seat> seatList = Lists.newArrayList();
        if(CollectionUtils.isEmpty(kindList)){
            return ServerResponse.createByErrorMsg("没有这个类型哦");
        }
        int rowCount=hallMapper.insert(hall);
        if(rowCount>0){
            for(Kind kinditem : kindList){
                Seat seat = new Seat();
                seat.setColumn(kinditem.getColumn());
                seat.setRow(kinditem.getRow());
                seat.setStatus(Const.SeatStatusEnum.SELECTABLE.getCode());
                seat.setHallId(hall.getId());
                seatList.add(seat);
            }
            //批量插入座位
            seatMapper.seatBatchInsert(seatList);
            return ServerResponse.createBySuccess("添加影厅成功");
        }
        return ServerResponse.createByErrorMsg("添加影厅失败");
    }

    //只是为了查询查找所有stype
    public ServerResponse findAllKind(){
        List<Kind> kindList =kindMapper.selectList();
        return ServerResponse.createBySuccess(kindList);
    }
    public ServerResponse updateHall(Hall hall){
        if(hall==null){
            return ServerResponse.createByErrorMsg("参数错误，请重新再试");
        }
        int rowCount =hallMapper.updateByPrimaryKey(hall);
        if(rowCount>0){
            return ServerResponse.createBySuccessMsg("更新放映厅成功");
        }
        return ServerResponse.createByErrorMsg("修改放映厅失败");
    }

    public ServerResponse<String> deleteHall(Integer hallId){
        if(hallId==null){
            return ServerResponse.createByErrorMsg("参数错误，请重新再试");
        }
        int rowCount =hallMapper.deleteByPrimaryKey(hallId);
        if(rowCount>0){
            return ServerResponse.createBySuccessMsg("删除放映厅成功");
        }
        seatMapper.deleteByHallId(hallId);
        return ServerResponse.createByErrorMsg("删除放映厅失败");
    }

    public ServerResponse<Hall> selectHall(Integer hallId){
        if(hallId==null){
            return ServerResponse.createByErrorMsg("参数错误，请重新再试");
        }
        Hall hall =hallMapper.selectByPrimaryKey(hallId);
        if(hall!=null){
            return ServerResponse.createBySuccess("查询放映厅成功",hall);
        }
        return ServerResponse.createByErrorMsg("查询放映厅失败");
    }

    public ServerResponse<String> setHallStatus(Integer hallId ,Integer status){
        if(hallId==null&&status==null){
            return ServerResponse.createByErrorMsg("参数错误，请重新再试");
        }
        Hall hall=new Hall();
        hall.setId(hallId);
        hall.setStatus(status);

        int rowCount=hallMapper.updateByPrimaryKeySelective(hall);
        if(rowCount>0){
            return ServerResponse.createBySuccessMsg("修改电影状态成功");
        }
        return ServerResponse.createByErrorMsg("修改电影状态失败");
    }

    public ServerResponse<PageInfo> hallList(int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Hall> hall =hallMapper.selectHallList();
        PageInfo pageInfo = new PageInfo(hall);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
