package com.kanke.controller.backend;

import com.github.pagehelper.PageInfo;
import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.Commodity_order;
import com.kanke.pojo.User;
import com.kanke.service.ICommodityOrderService;
import com.kanke.service.IUserService;
import com.kanke.vo.CommodityOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/CommodityOrder/")
public class CommodityOrderManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICommodityOrderService iCommodityOrderService;



    @RequestMapping(value="getCommodityOrderListManage.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> getCommodityOrderListManage(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(!iUserService.checkAdminRole(user).isSuccess()){
            return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
        }
        return iCommodityOrderService.getCommodityOrderListManage(pageNum,pageSize);
    }

    @RequestMapping(value="getCommodityOrderDetailManage.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CommodityOrderVo> getCommodityOrderDetailManage(HttpSession session, Long orderNo){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(!iUserService.checkAdminRole(user).isSuccess()){
            return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
        }
        return iCommodityOrderService.getCommodityOrderDetailManage(orderNo);
    }

    @RequestMapping(value="searchOrder.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> searchOrder(HttpSession session, Long orderNo, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if(!iUserService.checkAdminRole(user).isSuccess()){
            return ServerResponse.createByErrorMsg("不是管理员登录，无权限操作");
        }
        return iCommodityOrderService.searchOrder(orderNo,pageNum,pageSize);
    }
}
