package com.kanke.dao;

import com.kanke.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdAndCommodityId(@Param("userId") Integer userId,@Param("commodityId") Integer commodityId);

    List<Cart> selectByUserId(Integer userId);

    int selectStatusByUserId(Integer userId);

    int deleteByUserIdAndCommodityIds(Integer userId,List<String> commodityIdList);

    int checkedOrUnChecked(@Param("userId")Integer userId,@Param("commodityId")Integer commodityId,@Param("checked")Integer checked);

    int selectCount(@Param("userId") Integer userId);
}