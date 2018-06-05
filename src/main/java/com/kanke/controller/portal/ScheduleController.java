package com.kanke.controller.portal;

import com.github.pagehelper.PageInfo;
import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.User;
import com.kanke.service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/schedule/")
public class ScheduleController {
    @Autowired
    private IScheduleService iScheduleService;

    /**
     * 获取页面前端详情
     * @param movieId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="getDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> getDetail(Integer movieId,
                                              @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        return iScheduleService.getDetail(movieId,pageNum,pageSize);
    }

    /**
     * 获取所有排片
     * @param session
     * @param scheduleId
     * @return
     */
    @RequestMapping(value="getAllSchedule.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getAllSchedule(HttpSession session, Integer scheduleId){
//        User user=(User) session.getAttribute(Const.CURRENT_USER);
//        if(user==null){
//            return  ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
//        }
        return iScheduleService.getdetailById(scheduleId);
    }
}
