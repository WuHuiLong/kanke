package com.kanke.dao;

import com.kanke.pojo.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdAndOrderId(@Param("userId")Integer userId,@Param("orderNo") Long orderNo);

    Order selectByOrderNo(Long orderNo);

    int selectTotalPrice(Integer userId);

}