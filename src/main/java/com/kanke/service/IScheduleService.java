package com.kanke.service;

import com.github.pagehelper.PageInfo;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Schedule;
import com.kanke.vo.ScheduleVo;

import java.util.Date;

public interface IScheduleService {

    ServerResponse<ScheduleVo>deleteSchedule(Integer scheduleId);

    ServerResponse<ScheduleVo> detail(Integer scheduleId);

    ServerResponse checkConflict(Date startTime, Date endTime);

    ServerResponse<ScheduleVo> addAndUpdateSchedule(Schedule schedule);

    ServerResponse<PageInfo> getDetail(Integer movieId , int pageNum, int pageSize);

    ServerResponse<PageInfo> getDetailManege(int pageNum,int pageSize);

    ServerResponse<ScheduleVo> getdetailById(Integer scheduleId);
}
