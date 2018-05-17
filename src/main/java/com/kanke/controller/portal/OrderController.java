package com.kanke.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Interner;
import com.google.common.collect.Maps;
import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.*;
import com.kanke.service.IOrderService;
import com.kanke.vo.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger= LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private IOrderService iOrderService;
    /**
     * 下订单之前的展示
     * @param session
     * @param scheduleId
     * @param seatList
     * @return
     */
    @RequestMapping(value="show.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse show(HttpSession session, Integer scheduleId,List<Seat> seatList){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return  iOrderService.show(scheduleId,user.getId(),seatList);
    }
    /**
     * 创建订单和订单详情
     * @param session
     * @param scheduleId
     * @param seatList
     * @return
     */
    @RequestMapping(value="create.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse create(HttpSession session, Integer scheduleId,List<Seat> seatList){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return  iOrderService.creat(scheduleId,seatList,user.getId());
    }

    /**
     * 取消订单
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping(value="cancle.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse cancle(HttpSession session,Long orderNo){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return  iOrderService.cancle(user.getId(),orderNo);
    }

    /**
     * 获取前台订单详情
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping(value="detail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<OrderVo> detail(HttpSession session , Long orderNo){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getDetail(orderNo,user.getId());
    }

    /**
     * 用户查看订单详情
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="getOrderList.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> getOrderList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(),pageNum,pageSize);
    }

    /**
     * 支付宝付款接口
     * @param session
     * @param orderNo
     * @param request
     * @return
     */
    @RequestMapping(value="pay.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String path=request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo,user.getId(),path);
    }

    /**
     * 支付宝回调（网上抄的,并没有搞懂）
     * @param request
     * @return
     */
    @RequestMapping(value="alipayCallback.do",method = RequestMethod.POST)
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){ //object是因为与支付宝要求返回保持一致
        Map<String,String> params= Maps.newHashMap();

        //从支付宝中把参数的map都拿出来
        Map requestParams=request.getParameterMap();
        //遍历这些参数，取出其中的value值
        for (Iterator iterator=requestParams.keySet().iterator();iterator.hasNext();){
            String name=(String) iterator.next();
            String[] value=(String[]) requestParams.get(name);//获取key
            String valueStr="";
            for(int i=0;i<value.length;i++){
                if(i==value.length-1){
                    valueStr=valueStr+value[i];
                }else{
                    valueStr=valueStr+value[i]+",";//使用逗号把他们分割为一个个的字符串
                }
            }
            params.put(name,valueStr);
        }
        logger.info("支付宝回调，sign：{},trade_status:{},参数：{}",params.get("sign"),params.get("trade_status"),params.toString());

        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2= AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMsg("非法请求！！！");
            }
        } catch (AlipayApiException e) {
            logger.info("支付宝异常",e);
        }
        ServerResponse serverResponse = iOrderService.alipayCallback(params);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SECCESS;
        }
        return  Const.AlipayCallback.RESPONSE_FAILED;
    }

    /**
     * 检查用户支付状态
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping(value="queryOrderPayStatus.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session,Long orderNo){//Boolean是与前端的约定
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(orderNo,user.getId());
        if(!serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(false);
        }
        return  ServerResponse.createBySuccess(true);
    }
}
