package com.kanke.dao;

import com.kanke.pojo.Seat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SeatMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Seat record);

    int insertSelective(Seat record);

    Seat selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Seat record);

    int updateByPrimaryKey(Seat record);

    List<Seat> selectList(Integer hallId);

    List<Seat> selectSpecial(Integer hallId);

    void seatBatchInsert(@Param("seatList") List<Seat> seatList);

    int deleteByHallId(Integer hallId);
}