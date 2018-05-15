package com.kanke.dao;

import com.kanke.pojo.Commodity_order_item;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Commodity_order_itemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Commodity_order_item record);

    int insertSelective(Commodity_order_item record);

    Commodity_order_item selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Commodity_order_item record);

    int updateByPrimaryKey(Commodity_order_item record);

    List<Commodity_order_item> getByOrderNoUserId(@Param("userId") Integer userId,@Param("orderNo")Long orderNo);

    List<Commodity_order_item> getByOrderNo(Long orderNo);
}