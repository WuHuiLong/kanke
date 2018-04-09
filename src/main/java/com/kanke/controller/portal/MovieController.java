package com.kanke.controller.portal;

import com.github.pagehelper.PageInfo;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Movie;
import com.kanke.service.IMovieService;
import com.kanke.service.IUserService;
import com.kanke.vo.MovieDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/movie/")
public class MovieController {
    @Autowired
    private IMovieService iMovieService;

    /**
     * 前台电影详情
     * @param movieId
     * @return
     */
    @RequestMapping(value="detail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<MovieDetailVo> detail(Integer movieId){
        return iMovieService.detail(movieId);
    }

    /**
     * 前台电影搜索，分页，排序
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @RequestMapping(value="list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false) String keyword,
                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                         @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "")String orderBy){
        return iMovieService.list(keyword,categoryId,pageNum,pageSize,orderBy);
    }
}
