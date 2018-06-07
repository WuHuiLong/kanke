package com.kanke.controller.backend;

import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Hall;
import com.kanke.pojo.Schedule;
import com.kanke.pojo.User;
import com.kanke.service.IHallService;
import com.kanke.service.IMovieService;
import com.kanke.service.IScheduleService;
import com.kanke.service.IUserService;
import com.kanke.vo.ScheduleVo;
import net.sf.jsqlparser.schema.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/manage/schedule/")
public class ScheduleManageController {
    @Autowired
    private IScheduleService iScheduleService;

    @Autowired
    private IUserService iUserService;

//    /**
//     *查询放映厅信息
//     * @param session
//     * @param movieId
//     * @param hallId
//     * @return
//     */
//    @RequestMapping(value="selectSchedule.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ServerResponse selectSchedule(HttpSession session, Integer movieId,Integer hallId){
//        User user=(User) session.getAttribute(Const.CURRENT_USER);
//        if(user==null){
//            return  ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            return  iScheduleService.detail();
//        }
//        return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
//    }

    /**
     * 获取排片详情
     * @param session
     * @param scheduleId
     * @return
     */
    @RequestMapping(value="getDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session,Integer scheduleId){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return  iScheduleService.detail(scheduleId) ;
        }
        return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
    }

    /**
     * 检测时间是否冲突
     * @param session
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value="checkConflict.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse checkConflict(HttpSession session,Date startTime, Date endTime){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return  iScheduleService.checkConflict(startTime,endTime) ;
        }
        return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
    }

    /**
     * 添加排片,更新排片
     * @param session
     * @param schedule
     * @return
     */
    @RequestMapping(value="addSchedule.do",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<ScheduleVo> addSchedule(HttpSession session, Schedule schedule){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return  iScheduleService.addAndUpdateSchedule(schedule);
        }

        return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
    }

    /**
     * 删除排片
     * @param session
     * @param scheduleId
     * @return
     */
    @RequestMapping(value="deleteSchedule.do",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse deleteSchedule(HttpSession session,Integer scheduleId){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return  iScheduleService.deleteSchedule(scheduleId);
        }
        return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
    }

    /**
     * 获取所有排片（已经排好的电影）
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="AllSchedule.do",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse AllSchedule(HttpSession session,
                                       @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                       @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return  iScheduleService.getDetailManege(pageNum,pageSize);
        }
        return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
    }


}
