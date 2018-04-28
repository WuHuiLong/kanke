package com.kanke.controller.portal;

import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Hall;
import com.kanke.pojo.Seat;
import com.kanke.pojo.User;
import com.kanke.service.ISeatService;
import com.kanke.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/manage/seat")
public class SeatController {
    @Autowired
    private ISeatService iSeatService;

    /**
     * 获取整个放映厅所有座位
     * @param session
     * @param hallId
     * @return
     */
    @RequestMapping(value="getSeatDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<Seat>> getSeatDetail(HttpSession session,Integer hallId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iSeatService.getSeatDetail(hallId);
    }

    @RequestMapping(value="selectSeat.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse selectSeat(HttpSession session,Integer seatId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iSeatService.selectSeat(seatId);
    }


    @RequestMapping(value="updateSeatStatus.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateSeatStatus(HttpSession session,Integer seatId,Integer status){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iSeatService.updateSeatStatus(seatId,status);
    }

}
