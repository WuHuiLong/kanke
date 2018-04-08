package com.kanke.dao;

import com.kanke.pojo.Commodity_order;

public interface Commodity_orderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Commodity_order record);

    int insertSelective(Commodity_order record);

    Commodity_order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Commodity_order record);

    int updateByPrimaryKey(Commodity_order record);
}