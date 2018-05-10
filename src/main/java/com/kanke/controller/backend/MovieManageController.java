package com.kanke.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Movie;
import com.kanke.pojo.User;
import com.kanke.service.IFileService;
import com.kanke.service.IMovieService;
import com.kanke.service.IUserService;
import com.kanke.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;
import java.util.Map;

@Controller
@RequestMapping("/manage/movie/")
public class MovieManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IMovieService iMovieService;

    @Autowired
    private IFileService iFileService;

    /**
     * 更新电影和添加电影
     * @param session
     * @param movie
     * @return
     */
    @RequestMapping(value="movieSave.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse movieSave(HttpSession session, Movie movie){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return  iMovieService.saveOrUpdateMovie(movie);
        }
        return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
    }

    /**
     * 电影的上映和下架
     * @param session
     * @param movieId
     * @param status
     * @return
     */

    @RequestMapping(value="setSaleStatus.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer movieId,Integer status){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return  iMovieService.setSaleStatus(movieId,status);
        }
        return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
    }

    /**
     * 获取电影详情
     * @param session
     * @param movieId
     * @return
     */
    @RequestMapping(value="getDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session,Integer movieId){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return  iMovieService.manageMovieDetail(movieId) ;
        }
        return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
    }

    /**
     * 获取电影详情列表并分页
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value=" getList.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录后再试");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iMovieService.getMovieList(pageNum,pageSize);
        }
        return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
    }

    /**
     * 后台电影搜索
     * @param session
     * @param movieName
     * @param movieId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value=" movieSearch.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse movieSearch(HttpSession session,String movieName,Integer movieId,@RequestParam(value = "pageNum",
            defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录后再试");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iMovieService.searchMovie(movieName,movieId,pageNum,pageSize);
        }
        return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
    }

    /**
     * 文件上传
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value=" upload.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录后再试");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
//            String url= PropertiesUtil.getProperties("ftp.server.http.prefix")+targetFileName;
            Map fileMap= Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",path);
            return ServerResponse.createBySuccess(targetFileName);
        }
        return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
    }

    /**
     * 富文本文件上传,使用simditor,需要按照simditor来进行返回
     * @param session
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="richtextImgUpload.do",method = RequestMethod.POST)
    @ResponseBody
    public Map richTextUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file,
                              HttpServletRequest request, HttpServletResponse response){
        Map resultMap=Maps.newHashMap();
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员账号");
            return resultMap;
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            String path=request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url= PropertiesUtil.getProperties("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",path);//ftp上传
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }
        resultMap.put("success",false);
        resultMap.put("msg","无权限操作");
        return resultMap;
    }
}
