package com.kanke.controller.portal;

import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.User;
import com.kanke.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;
    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value="login.do",method = RequestMethod.POST)
    @ResponseBody //在返回时自动通过MVC的插件将我们的返回值序列化为json
    public ServerResponse<User> login(String username, String password, HttpSession session){
        //service->mybatis->dao
        ServerResponse<User> response=iUserService.login(username,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @RequestMapping(value="logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value="register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    /**
     * 用户校验
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value="checkValid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){//通过str传参，用type来分辨是用户名还是邮箱
        return iUserService.checkValid(str,type);
    }
    /**
     * 获取用户登录信息
     */
    @RequestMapping(value="getUserInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMsg("用户未登录，无法返回信息");
    }

    /**
     * 密码问题的获取
     * @param username
     * @return
     */
    @RequestMapping(value="forgetGetQuestion.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    /**
     * 使用本地缓存检查问题答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value="forgetCheckAnswer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    /**
     * 忘记密码后的重置密码
     * @param username
     * @param newPassword
     * @param forgetToken
     * @return
     */
    @RequestMapping(value="forgetResetPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String newPassword,String forgetToken){
        return iUserService.forgetResetPassword(username,newPassword,forgetToken);
    }

    /**
     * 登录时重置密码
     * @param session
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping(value="resetPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String oldPassword,String newPassword){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        return iUserService.resetPassword(oldPassword,newPassword,user);
    }

    @RequestMapping(value="update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session,User user){
        User currentUser=(User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        //user传过来的信息当中不包括Userid，所以我们要把当前用户的userid传过来
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());

        ServerResponse<User> response = iUserService.update_information(user);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 查看用户详细信息
     * 在更新个人信息前，先进行个人账户强制登录，这样，更新时只需要查看是否已经登录就行了
     * @param session
     * @return
     */
    @RequestMapping(value="get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session){
        User currentUser=(User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"未登录，需强制登录");
        }
        return iUserService.get_information(currentUser.getId());
    }
}
