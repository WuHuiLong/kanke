package com.kanke.dao;

import com.kanke.pojo.Commodity_order_item;

public interface Commodity_order_itemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Commodity_order_item record);

    int insertSelective(Commodity_order_item record);

    Commodity_order_item selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Commodity_order_item record);

    int updateByPrimaryKey(Commodity_order_item record);
}