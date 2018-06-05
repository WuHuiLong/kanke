package com.kanke.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.dao.HallMapper;
import com.kanke.dao.MovieMapper;
import com.kanke.dao.ScheduleMapper;
import com.kanke.pojo.Hall;
import com.kanke.pojo.Movie;
import com.kanke.pojo.Schedule;
import com.kanke.service.IScheduleService;
import com.kanke.util.DateTimeUtil;
import com.kanke.vo.ScheduleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;
import java.util.Date;
import java.util.List;

@Service("iScheduleService")
public class IScheduleServiceImpl implements IScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private HallMapper hallMapper;

//    public ServerResponse<ScheduleVo> selectSchedule(Integer movieId,Integer hallId){
//        if(movieId==null&&hallId==null){
//            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
//        }
//        Schedule schedule=scheduleMapper.selectScheduleByMovieIdHallId(movieId,hallId);
//        if(schedule==null){
//            return ServerResponse.createByErrorMsg("暂时还未排片");
//        }
//        ScheduleVo scheduleVo=getScheduleVo(schedule);
//        //查询时不需要管是否冲突（已经排好了，不需要你操心了）
////        ServerResponse conflictResponse=this.checkConflict(DateTimeUtil.strToDate(scheduleVo.getStartTime()),DateTimeUtil.strToDate(scheduleVo.getEndTime1()));
////        if(!conflictResponse.isSuccess()){
////            return conflictResponse;
////        }
//        return ServerResponse.createBySuccess(scheduleVo);
//    }

    public ServerResponse<ScheduleVo>deleteSchedule(Integer scheduleId){
        if(scheduleId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int rowCount=scheduleMapper.deleteByPrimaryKey(scheduleId);
        if(rowCount>0){
            return ServerResponse.createBySuccessMsg("删除排片成功");
        }
        return ServerResponse.createByErrorMsg("删除拍片失败");
    }
    public ServerResponse<ScheduleVo> detail(Integer scheduleId){
        if(scheduleId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Schedule schedule=scheduleMapper.selectByPrimaryKey(scheduleId);
        if(schedule==null){
            return ServerResponse.createByErrorMsg("暂时还未排片");
        }
        ScheduleVo scheduleVo=getScheduleVo(schedule);
        return ServerResponse.createBySuccess(scheduleVo);
    }

    public ServerResponse checkConflict(Date startTime,Date endTime){
        if(startTime==null&&endTime==null){
            return ServerResponse.createByErrorMsg("参数错误");
        }
        int rowCount=scheduleMapper.checkConflict(startTime,endTime);
        if(rowCount>0){
            return  ServerResponse.createByErrorMsg("时间冲突，请重新排片");
        }
        return ServerResponse.createBySuccessMsg("时间不冲突，排片成功");
    }

//    public ServerResponse<ScheduleVo> addAndUpdateSchedule(Integer movieId,Integer hallId,Date startTime){
//        if(movieId==null&&hallId==null&&startTime==null){
//            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
//        }
//        Schedule scheduleItem=new Schedule();
//        scheduleItem.setMovieId(movieId);
//        scheduleItem.setHallId(hallId);
//        scheduleItem.setStartTime(startTime);
//        Movie movie=movieMapper.selectByPrimaryKey(movieId);
//        if(movie==null){
//            return  ServerResponse.createByErrorMsg("参数错误");
//        }
//        scheduleItem.setEndTime(DateTimeUtil.strToDate(DateTimeUtil.dateToint(startTime,Const.HALFTIME,movie.getLength())));
//        ServerResponse conflictResponse=this.checkConflict(startTime,DateTimeUtil.strToDate(DateTimeUtil.dateToint(startTime,Const.HALFTIME,movie.getLength())));
//        if(!conflictResponse.isSuccess()){
//            return conflictResponse;
//        }
//        scheduleMapper.insert(scheduleItem);
//        ScheduleVo scheduleVo=getScheduleVo(scheduleItem);
//        return ServerResponse.createBySuccess(scheduleVo);
//    }
    public ServerResponse<ScheduleVo> addAndUpdateSchedule(Schedule schedule){
        if(schedule==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if(schedule.getId()==null){
            Movie movie=movieMapper.selectByPrimaryKey(schedule.getMovieId());
            if(movie==null){
                return ServerResponse.createByErrorMsg("参数错误");
            }
            schedule.setPrice(movie.getPrice());
            schedule.setEndTime(DateTimeUtil.strToDate(DateTimeUtil.dateToint(schedule.getStartTime(),Const.HALFTIME,movie.getLength())));
            ServerResponse serverResponse=this.checkConflict(schedule.getStartTime(),schedule.getEndTime());
            if(!serverResponse.isSuccess()){
                return serverResponse;
            }
            int rowCount=scheduleMapper.insert(schedule);
            ScheduleVo scheduleVo=getScheduleVo(schedule);
            if(rowCount>0){
                return ServerResponse.createBySuccess("添加排片成功",scheduleVo);
            }
            return ServerResponse.createByErrorMsg("添加排片失败");

        }else{
            Movie movie=movieMapper.selectByPrimaryKey(schedule.getMovieId());
            if(movie==null){
                return ServerResponse.createByErrorMsg("参数错误");
            }
            schedule.setEndTime(DateTimeUtil.strToDate(DateTimeUtil.dateToint(schedule.getStartTime(),Const.HALFTIME,movie.getLength())));
            ServerResponse serverResponse=this.checkConflict(schedule.getStartTime(),schedule.getEndTime());
            if(!serverResponse.isSuccess()){
                return serverResponse;
            }
            int rowCount=scheduleMapper.updateByPrimaryKey(schedule);
            ScheduleVo scheduleVo=getScheduleVo(schedule);
            if(rowCount>0){
                return ServerResponse.createBySuccess("修改排片成功",scheduleVo);
            }
            return ServerResponse.createByErrorMsg("修改排片失败");
        }
    }

    private ScheduleVo getScheduleVo(Schedule schedule){
        ScheduleVo scheduleVo=new ScheduleVo();
        scheduleVo.setId(schedule.getId());
        scheduleVo.setMovieId(schedule.getMovieId());
        scheduleVo.setHallId(schedule.getHallId());
        scheduleVo.setStartTime(DateTimeUtil.DateTostr(schedule.getStartTime()));
        scheduleVo.setCreateTime(DateTimeUtil.DateTostr(schedule.getCreateTime()));
        scheduleVo.setUpdateTime(DateTimeUtil.DateTostr(schedule.getUpdateTime()));

        Movie movie=movieMapper.selectByPrimaryKey(schedule.getMovieId());
        scheduleVo.setMovieName(movie.getName());
        scheduleVo.setMovieLength(movie.getLength());
        scheduleVo.setPrice(movie.getPrice());

        Hall hall=hallMapper.selectByPrimaryKey(schedule.getHallId());
        scheduleVo.setHallName(hall.getName());
        scheduleVo.setEndTime1(DateTimeUtil.dateToint(schedule.getStartTime(),Const.HALFTIME,movie.getLength()));

        return scheduleVo;
    }

    //前台板块

    public ServerResponse<PageInfo> getDetail(Integer movieId ,int pageNum,int pageSize){
        if(movieId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Schedule> scheduleList=scheduleMapper.selectByMovieId(movieId);
        List<ScheduleVo> scheduleVoList= Lists.newArrayList();
        for(Schedule scheduleItem : scheduleList){
            ScheduleVo scheduleVo=getScheduleVo(scheduleItem);
            scheduleVoList.add(scheduleVo);
        }
        PageInfo pageInfo=new PageInfo(scheduleList);
        pageInfo.setList(scheduleVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    //后台获取所有排片及分页
    public ServerResponse<PageInfo> getDetailManege(int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Schedule> scheduleList=scheduleMapper.selectList();
        List<ScheduleVo> scheduleVoList= Lists.newArrayList();
        for(Schedule scheduleItem : scheduleList){
            ScheduleVo scheduleVo=getScheduleVo(scheduleItem);
            scheduleVoList.add(scheduleVo);
        }
        PageInfo pageInfo=new PageInfo(scheduleList);
        pageInfo.setList(scheduleVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    //查询
    public ServerResponse<ScheduleVo> getdetailById(Integer scheduleId){
        if(scheduleId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Schedule schedule = scheduleMapper.selectByPrimaryKey(scheduleId);
        ScheduleVo scheduleVo =getScheduleVo(schedule);
        if(schedule==null){
            return ServerResponse.createByErrorMsg("还没拍片呢，下次再来吧");
        }
        return ServerResponse.createBySuccess("排片完成",scheduleVo);
    }

}
