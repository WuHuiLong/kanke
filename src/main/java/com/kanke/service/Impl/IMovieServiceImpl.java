package com.kanke.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.dao.CategoryMapper;
import com.kanke.dao.MovieMapper;
import com.kanke.pojo.Category;
import com.kanke.pojo.Movie;
import com.kanke.service.ICategoryService;
import com.kanke.service.IMovieService;
import com.kanke.util.DateTimeUtil;
import com.kanke.util.PropertiesUtil;
import com.kanke.vo.MovieDetailVo;
import com.kanke.vo.MovieListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iMovieService")
public class IMovieServiceImpl implements IMovieService{
    @Autowired
    private MovieMapper movieMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;
    /**
     * 通过主键更新商品，通过新增主键来添加商品
     * @param movie
     * @return
     */
    public ServerResponse saveOrUpdateMovie(Movie movie){
        if(movie!=null){
            if(StringUtils.isNotBlank(movie.getSubImages())){
                String[] subImages=movie.getSubImages().split(",");
                if(subImages.length>0){
                    movie.setMainImage(subImages[0]);
                }
            }
            if(movie.getId()!=null){
                int rowCount=movieMapper.updateByPrimaryKey(movie);
                if(rowCount>0){
                    return ServerResponse.createBySuccessMsg("更新电影成功");
                }
                return ServerResponse.createByErrorMsg("更新电影失败");
            }else{
                int rowCount =movieMapper.insert(movie);
                if(rowCount>0){
                    return ServerResponse.createBySuccessMsg("添加电影成功");
                }
                return ServerResponse.createByErrorMsg("添加电影失败");
            }
        }
        return ServerResponse.createByErrorMsg("参数错误，请稍后再试");
    }

    public ServerResponse<String> setSaleStatus(Integer movieId,Integer status){
        if(movieId==null&&status==null){
            return ServerResponse.createByErrorMsg("参数错误，请稍后再试");
        }
        Movie movie=new Movie();
        movie.setId(movieId);
        movie.setStatus(status);

        int rowCount=movieMapper.updateByPrimaryKeySelective(movie);
        if(rowCount>0){
            return ServerResponse.createBySuccessMsg("修改电影状态成功");
        }
        return  ServerResponse.createByErrorMsg("修改电影状态失败");
    }

    public ServerResponse<MovieDetailVo> manageMovieDetail(Integer movieId){
        if(movieId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Movie movie=movieMapper.selectByPrimaryKey(movieId);
        if(movie==null){
            return ServerResponse.createByErrorMsg("电影已经下映或删除");
        }
        //VO对象->value Object
        //pojo->bo(business Object)->vo(view object)
        MovieDetailVo movieDetailVo=assembleMovieDetailVo(movie);
        return ServerResponse.createBySuccess(movieDetailVo);
    }

    private MovieDetailVo assembleMovieDetailVo(Movie movie){
        MovieDetailVo movieDetailVo=new MovieDetailVo();
        movieDetailVo.setId(movie.getId());
        movieDetailVo.setDetail(movie.getDetail());
        movieDetailVo.setDirector(movie.getDirector());
        movieDetailVo.setLanguage(movie.getLanguage());
        movieDetailVo.setLength(movie.getLength());
        movieDetailVo.setMain_image(movie.getMainImage());
        movieDetailVo.setMovie_address(movie.getMovieAddress());
        movieDetailVo.setName(movie.getName());
        movieDetailVo.setPrice(movie.getPrice());
        movieDetailVo.setStarring(movie.getStarring());
        movieDetailVo.setStatus(movie.getStatus());
        movieDetailVo.setCategoryId(movie.getCategoryId());
        movieDetailVo.setSub_image(movie.getSubImages());
        //这一处需要修改
        movieDetailVo.setImageHost(PropertiesUtil.getProperties("ftp.server.http.prefix","http://img.happymmall.com/"));
        Category category= (Category) categoryMapper.selectCategoryChildrenByParentId(movie.getCategoryId());
        if(category==null){
            movieDetailVo.setCategoryId(0);
        }else{
            movieDetailVo.setParentCategoryId(category.getParentId());
        }

        movieDetailVo.setCreatetime(DateTimeUtil.DateTostr(movie.getCreateTime()));
        movieDetailVo.setUpdatetime(DateTimeUtil.DateTostr(movie.getUpdateTime()));

        return movieDetailVo;
    }
    public ServerResponse<PageInfo> getMovieList(int pageNum,int pageSize){
        //startpage-start
        //填充自己的sql逻辑
        //pageHelper收尾
        PageHelper.startPage(pageNum,pageSize);
        List<Movie> movieList= movieMapper.selectList();
        List<MovieListVo> movieListVoList=Lists.newArrayList();
        for(Movie movieItem : movieList){
            MovieListVo movieListVo=assembleMovieListVo(movieItem);
            movieListVoList.add(movieListVo);
        }
        PageInfo pageInfo=new PageInfo(movieList);
        pageInfo.setList(movieListVoList);
        return  ServerResponse.createBySuccess(pageInfo);
    }

    private MovieListVo assembleMovieListVo(Movie movie){
        MovieListVo movieListVo=new MovieListVo();
        movieListVo.setId(movie.getId());
        movieListVo.setDirector(movie.getDirector());
        movieListVo.setLanguage(movie.getLanguage());
        movieListVo.setLength(movie.getLength());
        movieListVo.setMain_image(movie.getMainImage());
        movieListVo.setMovie_address(movie.getMovieAddress());
        movieListVo.setName(movie.getName());
        movieListVo.setPrice(movie.getPrice());
        movieListVo.setStarring(movie.getStarring());
        movieListVo.setStatus(movie.getStatus());
        movieListVo.setCategoryId(movie.getCategoryId());
        //这一处需要修改
        movieListVo.setImageHost(PropertiesUtil.getProperties("ftp.server.http.prefix","http://img.happymmall.com/"));
        return movieListVo;
    }

    public ServerResponse<PageInfo> searchMovie(String movieName,Integer movieId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(movieName)){
            movieName=new StringBuilder().append("%").append(movieName).append("%").toString();
        }
        List<Movie> movieList= movieMapper.selectByNameAndMovieId(movieName,movieId);
        List<MovieListVo> movieListVoList=Lists.newArrayList();
        for(Movie movieItem : movieList){
            MovieListVo movieListVo=assembleMovieListVo(movieItem);
            movieListVoList.add(movieListVo);
        }
        PageInfo pageInfo=new PageInfo(movieList);
        pageInfo.setList(movieListVoList);
        return  ServerResponse.createBySuccess(pageInfo);
    }

    //前台板块
    public ServerResponse<MovieDetailVo> detail(Integer movieId){
        if(movieId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Movie movie=movieMapper.selectByPrimaryKey(movieId);
        if(movie==null){
            return ServerResponse.createByErrorMsg("电影已经下映或删除");
        }
        if(movie.getStatus()!= Const.MovieStatusEnum.ON_SALE.getCode()){
            return ServerResponse.createByErrorMsg("电影已经下映或删除");
        }
        MovieDetailVo movieDetailVo=assembleMovieDetailVo(movie);
        return ServerResponse.createBySuccess(movieDetailVo);
    }


    public ServerResponse<PageInfo> list(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
        if(StringUtils.isBlank(keyword)&&categoryId==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList=new ArrayList<Integer>();
        if(categoryId!=null){
            Category category=categoryMapper.selectByPrimaryKey(categoryId);
            if(category==null&&StringUtils.isBlank(keyword)){//相当于一个前端没有的分类
                PageHelper.startPage(pageNum,pageSize);
                List<MovieDetailVo> movieDetailVoList=Lists.newArrayList();
                PageInfo pageInfo=new PageInfo(movieDetailVoList);
                pageInfo.setList(movieDetailVoList);
            }
            categoryIdList=iCategoryService.selectCategoryAndChildById(category.getId()).getData();
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword=new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        //排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.MovieListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] a=orderBy.split("_");
                PageHelper.orderBy(a[0]+""+a[1]);
            }
        }
        List<Movie> movieList=movieMapper.selectByNameAndCategoryId(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
        List<MovieListVo> movieListVoList=Lists.newArrayList();
        for(Movie movieItem : movieList){
            MovieListVo movieListVo=assembleMovieListVo(movieItem);
            movieListVoList.add(movieListVo);
        }
        PageInfo pageInfo=new PageInfo(movieList);
        pageInfo.setList(movieListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

}
