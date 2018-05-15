package com.kanke.controller.backend;

import com.github.pagehelper.PageInfo;
import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Order;
import com.kanke.pojo.User;
import com.kanke.service.IOrderService;
import com.kanke.service.IUserService;
import com.kanke.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderService iOrderService;

    /**
     * 获得订单列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="orderList.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(!iUserService.checkAdminRole(user).isSuccess()){
            return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
        }
        return  iOrderService.OrderListManage(pageNum,pageSize);
    }

    /**
     * 获得订单详情
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping(value="manageOrderDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<OrderVo> manageOrderDetail(HttpSession session, Long orderNo){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(!iUserService.checkAdminRole(user).isSuccess()){
            return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
        }
        return  iOrderService.getDetailManage(orderNo);
    }

    /**
     * 后台查询订单
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping(value="searchOrder.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> searchOrder(HttpSession session, Long orderNo, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(!iUserService.checkAdminRole(user).isSuccess()){
            return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
        }
        return  iOrderService.searchOrder(orderNo,pageNum,pageSize);
    }

}
