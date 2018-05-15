package com.kanke.service.Impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.kanke.commom.Const;
import com.kanke.commom.ResponseCode;
import com.kanke.commom.ServerResponse;
import com.kanke.dao.CartMapper;
import com.kanke.dao.CommodityMapper;
import com.kanke.pojo.Cart;
import com.kanke.pojo.Commodity;
import com.kanke.service.ICartService;
import com.kanke.util.BigDecimalUtil;
import com.kanke.util.PropertiesUtil;
import com.kanke.vo.CartCommodityVo;
import com.kanke.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("iCartService")
public class ICartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CommodityMapper commodityMapper;

    public ServerResponse<CartVo> add(Integer commodityId,Integer userId,Integer count){
        if(commodityId==null||count==null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart=cartMapper.selectByUserIdAndCommodityId(userId,commodityId);
        if(cart==null){
            //此时购物车中无此商品记录
            Cart cartItem=new Cart();
            cartItem.setUserId(userId);
            cartItem.setCommodityId(commodityId);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setQuantity(count);
            cartMapper.insert(cartItem);
        }
        else{
            count = cart.getQuantity()+count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    //好好看sql语句，难点
    public ServerResponse<CartVo> delectCommodity(Integer userId,String commodityIds){
        List<String> commodityList = Splitter.on(",").splitToList(commodityIds);
        if(CollectionUtils.isEmpty(commodityList)){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdAndCommodityIds(userId,commodityList);
        return this.list(userId);
    }

    //修改只能修改数量
    public ServerResponse<CartVo> update(Integer userId,Integer commodityId,Integer count){
        if(commodityId == null){
            return ServerResponse.createByError(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart =cartMapper.selectByUserIdAndCommodityId(userId,commodityId);
        if(cart != null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKey(cart);
        return this.list(userId);
    }

    public ServerResponse<CartVo> list(Integer userId){
        CartVo cartVo =this.getCartVo(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    public ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer commodityId,Integer checked){
        cartMapper.checkedOrUnChecked(userId,commodityId,checked);
        return this.list(userId);
    }

    public ServerResponse<Integer> getCarCommodityQuantity(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        int count =cartMapper.selectCount(userId);
        return ServerResponse.createBySuccess(count);
    }


    //购物车页面展示
    private CartVo getCartVo(Integer userId){
        CartVo cartVo =new CartVo();
        List<Cart> cartList =cartMapper.selectByUserId(userId);
        List<CartCommodityVo> cartCommodityVoList = Lists.newArrayList();

        //购物车所有商品的价格
        BigDecimal carTolPrice =new BigDecimal("0");

        if(CollectionUtils.isNotEmpty(cartList)){
            for(Cart cart : cartList){
                CartCommodityVo cartCommodityVo =new CartCommodityVo();
                cartCommodityVo.setCommodityId(cart.getCommodityId());
                cartCommodityVo.setUserId(userId);
                cartCommodityVo.setId(cart.getId());

                Commodity commodity = commodityMapper.selectByPrimaryKey(cart.getCommodityId());
                if(commodity!=null){
                    cartCommodityVo.setCommodityMainImage(commodity.getMainImage());
                    cartCommodityVo.setCommodityName(commodity.getName());
                    cartCommodityVo.setCommodityPrice(commodity.getPrice());
                    cartCommodityVo.setCommodityStatus(commodity.getStatus());
                    cartCommodityVo.setCommodityStock(commodity.getStock());
                    //判断库存
                    int buyCount = 0;
                    if(commodity.getStock()>cart.getQuantity()){
                        buyCount = cart.getQuantity();
                        cartCommodityVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyCount =commodity.getStock();
                        cartCommodityVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //东西卖完了，更新数据库
                        Cart cart1 =new Cart();
                        cart1.setQuantity(buyCount);
                        cart1.setId(cart.getId());
                        cartMapper.updateByPrimaryKeySelective(cart1);
                    }
                    cartCommodityVo.setQuantity(buyCount);
                    cartCommodityVo.setCommodityChecked(cart.getChecked());
                    //不能使用cart.getQuantity()，因为如果库存不够会造成商品买卖的错误发生
                    cartCommodityVo.setCommodityTotalPrice(BigDecimalUtil.mul(commodity.getPrice().doubleValue(),cartCommodityVo.getQuantity()));
                }
                if(cart.getChecked() == Const.Cart.CHECKED){
                    carTolPrice =BigDecimalUtil.add(cartCommodityVo.getCommodityTotalPrice().doubleValue(),carTolPrice.doubleValue());
                }
                cartCommodityVoList.add(cartCommodityVo);
            }
        }
        cartVo.setCartCommodityVoList(cartCommodityVoList);
        cartVo.setCartTotalPrice(carTolPrice);
        cartVo.setImageHost(PropertiesUtil.getProperties("ftp.server.http.prefix"));
        cartVo.setAllChecked(this.istrue(userId));

        return cartVo;
    }

    private boolean istrue(Integer userId){
        if(userId == null){
            return false;
        }
        return  cartMapper.selectStatusByUserId(userId) == 0;
    }
}
