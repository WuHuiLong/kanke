package com.kanke.controller.backend;


import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Hall;
import com.kanke.pojo.User;
import com.kanke.service.IHallService;
import com.kanke.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class HallManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IHallService iHallService;

    /**
     *
     * @param session
     * @param hall
     * @return
     */
    @RequestMapping(value="addHall.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addHall(HttpSession session, Hall hall){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iHallService.addHall(hall);
        }
        return ServerResponse.createByErrorMsg("不是管理员登录,无权限操作");
    }

    /**
     * 更新放映厅
     * @param session
     * @param hall
     * @return
     */
    @RequestMapping(value="updateHall.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateHall(HttpSession session,Hall hall){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iHallService.updateHall(hall);
        }
        return ServerResponse.createByErrorMsg("不是管理员登录,无权限操作");
    }

    /**
     * 删除放映厅
     * @param session
     * @param hallId
     * @return
     */
    @RequestMapping(value="deleteHall.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> deleteHall(HttpSession session,Integer hallId){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iHallService.deleteHall(hallId);
        }
        return ServerResponse.createByErrorMsg("不是管理员登录,无权限操作");
    }

    /**
     * 查询放映厅
     * @param session
     * @param hallId
     * @return
     */
    @RequestMapping(value="selectHall.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Hall> selectHall(HttpSession session,Integer hallId){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iHallService.selectHall(hallId);
        }
        return ServerResponse.createByErrorMsg("不是管理员登录,无权限操作");
    }

    /**
     * 电影院状态修改
     * @param session
     * @param hallId
     * @param status
     * @return
     */
    @RequestMapping(value="setHallStatus.do",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<String> setHallStatus(HttpSession session,Integer hallId,Integer status){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iHallService.setHallStatus(hallId,status);
        }
        return ServerResponse.createByErrorMsg("不是管理员登录,无权限操作");
    }

}
