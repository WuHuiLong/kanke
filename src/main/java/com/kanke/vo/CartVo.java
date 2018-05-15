package com.kanke.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartVo {

    private List<CartCommodityVo> cartCommodityVoList;

    private BigDecimal cartTotalPrice;

    private String imageHost;

    private Boolean AllChecked;
}
