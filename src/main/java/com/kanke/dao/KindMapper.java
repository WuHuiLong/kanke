package com.kanke.dao;

import com.kanke.pojo.Kind;

import java.util.List;

public interface KindMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Kind record);

    int insertSelective(Kind record);

    Kind selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Kind record);

    int updateByPrimaryKey(Kind record);

    List<Kind> selectByStype(String stype);

    List<Kind> selectList();
}