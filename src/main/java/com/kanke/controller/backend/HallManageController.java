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

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class HallManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IHallService iHallService;

    public ServerResponse addHall(HttpSession session, Hall hall){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){

        }
        return ServerResponse.createByErrorMsg("不是管理员登录,无权限操作");
    }

}
