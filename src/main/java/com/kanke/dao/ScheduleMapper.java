package com.kanke.dao;

import com.kanke.pojo.Schedule;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ScheduleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Schedule record);

    int insertSelective(Schedule record);

    Schedule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Schedule record);

    int updateByPrimaryKey(Schedule record);

    Schedule selectScheduleByMovieIdHallId(@Param("movidId") Integer movidId,@Param("hallId") Integer hallId);

    int checkConflict(@Param("hallId") Integer hallId,@Param("startTime")Date startTime,@Param("endTime")Date endTime);

    List<Schedule> selectByMovieId(Integer movieId);

    List<Schedule> selectList();

    int selectScheduleId();
}