package com.kanke.dao;

import com.kanke.pojo.Hall;

import java.util.List;

public interface HallMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Hall record);

    int insertSelective(Hall record);

    Hall selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Hall record);

    int updateByPrimaryKey(Hall record);

    List<Hall> selectHallList();
}