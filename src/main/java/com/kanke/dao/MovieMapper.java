package com.kanke.dao;

import com.kanke.pojo.Movie;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MovieMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Movie record);

    int insertSelective(Movie record);

    Movie selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Movie record);

    int updateByPrimaryKey(Movie record);

    List<Movie> selectList();

    List<Movie> selectListManage();

    List<Movie> selectByNameAndMovieId(@Param("movieName") String movieName, @Param("movieId") Integer movieId);

    List<Movie> selectByNameAndCategoryId(@Param("movieName") String movieName,@Param("categoryList") List<Integer> categoryList);
}