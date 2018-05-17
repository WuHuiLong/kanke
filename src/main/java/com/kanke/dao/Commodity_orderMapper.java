package com.kanke.dao;

import com.kanke.pojo.Commodity_order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Commodity_orderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Commodity_order record);

    int insertSelective(Commodity_order record);

    Commodity_order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Commodity_order record);

    int updateByPrimaryKey(Commodity_order record);

    Commodity_order selectByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo")Long orderNo);

    Commodity_order selectByOrderNo(Long orderNo);

    List<Commodity_order> selectByUserId(Integer userId);

    List<Commodity_order> selectList();
}