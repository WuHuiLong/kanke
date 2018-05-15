package com.kanke.dao;

import com.kanke.pojo.Commodity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommodityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Commodity record);

    int insertSelective(Commodity record);

    Commodity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Commodity record);

    int updateByPrimaryKey(Commodity record);

    List<Commodity> selectListManage();

    List<Commodity> searchCommodityByNameAndId(@Param("commodityName") String commodityName,@Param("commodityId")Integer commodityId);

    List<Commodity> searchCommodityByKeyword(@Param("commodityName")String commodityName);

    List<Commodity> selectAll();
}