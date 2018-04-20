package com.kanke.controller.portal;

import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Hall;
import com.kanke.pojo.Seat;
import com.kanke.service.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/manage/seat")
public class SeatController {
    @Autowired
    private ISeatService iSeatService;

    public ServerResponse<Seat> getSeatDetail(Seat seat){
        return null;
    }
}
