package com.kanke.controller.portal;

import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.pojo.User;
import com.kanke.service.ICartService;
import com.kanke.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private ICartService iCartService;

    /**
     * 向购物车添加商品
     * @param session
     * @param commodityId
     * @param count
     * @return
     */
    @RequestMapping(value="addCommodity.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> addCommodity(HttpSession session,Integer commodityId, Integer count){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(commodityId,user.getId(),count);
    }

    /**
     * 展示购物车所有商品
     * @param session
     * @return
     */
    @RequestMapping(value="list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    /**
     * 修改要购买商品的数量
     * @param session
     * @param commodityId
     * @param count
     * @return
     */
    @RequestMapping(value="update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session,Integer commodityId, Integer count){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(),commodityId,count);
    }

    /**
     * 删除购物车中商品（商品数量可以多个，但是一次只能删除一种商品）
     * @param session
     * @param commodityIds
     * @return
     */
    @RequestMapping(value="delete.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpSession session,String commodityIds){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.delectCommodity(user.getId(),commodityIds);
    }

    /**
     * 全选
     * @param session
     * @return
     */
    @RequestMapping(value="selectAll.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
    }

    /**
     * 全反选
     * @param session
     * @return
     */
    @RequestMapping(value="unSelectAll.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
    }

    /**
     * 取消一个
     * @param session
     * @param commodityId
     * @param checked
     * @return
     */
    @RequestMapping(value="UnSelectOne.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> UnSelectOne(HttpSession session,Integer commodityId){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),commodityId,Const.Cart.UN_CHECKED);
    }

    /**
     * 选择一个
     * @param session
     * @param commodityId
     * @return
     */
    @RequestMapping(value="SelectOne.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> SelectOne(HttpSession session,Integer commodityId){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(),commodityId,Const.Cart.CHECKED);
    }

    /**
     * 获取购物车商品数量
     * @param session
     * @return
     */
    @RequestMapping(value="getCount.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Integer> getCount(HttpSession session){
        User user =(User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCarCommodityQuantity(user.getId());
    }
}
