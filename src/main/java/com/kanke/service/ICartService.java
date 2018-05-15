package com.kanke.service;

import com.kanke.commom.ServerResponse;
import com.kanke.vo.CartVo;

public interface ICartService {

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> add(Integer commodityId,Integer userId,Integer count);

    ServerResponse<CartVo> delectCommodity(Integer userId,String commodityIds);

    ServerResponse<CartVo> update(Integer userId,Integer commodityId,Integer count);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer commodityId,Integer checked);

    ServerResponse<Integer> getCarCommodityQuantity(Integer userId);
}
