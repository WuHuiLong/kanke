package com.kanke.dao;

import com.kanke.pojo.commodity;

public interface commodityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(commodity record);

    int insertSelective(commodity record);

    commodity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(commodity record);

    int updateByPrimaryKey(commodity record);
}