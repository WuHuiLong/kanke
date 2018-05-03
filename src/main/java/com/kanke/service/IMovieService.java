package com.kanke.service;

import com.github.pagehelper.PageInfo;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Movie;
import com.kanke.vo.MovieDetailVo;

public interface IMovieService {
    ServerResponse saveOrUpdateMovie(Movie movie);

    ServerResponse<String> setSaleStatus(Integer movieId,Integer status);

    ServerResponse<MovieDetailVo> manageMovieDetail(Integer movieId);

    ServerResponse<PageInfo> getMovieList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchMovie(String movieName,Integer movieId,int pageNum,int pageSize);

    ServerResponse<MovieDetailVo> detail(Integer movieId);

    ServerResponse<PageInfo> list(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);

    ServerResponse<PageInfo> AllSelect(int pageNum , int pageSize);
}
