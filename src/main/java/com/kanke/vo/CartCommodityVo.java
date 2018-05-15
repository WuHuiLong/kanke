package com.kanke.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartCommodityVo {
    private Integer id;
    private Integer userId;
    private Integer commodityId;
    private Integer quantity;//购物车中此商品的数量
    private String commodityName;
    private String commodityMainImage;
    private BigDecimal commodityPrice;
    private Integer commodityStatus;
    private BigDecimal commodityTotalPrice;
    private Integer commodityStock;
    private Integer commodityChecked;//此商品是否勾选

    private String limitQuantity;//限制数量的一个返回结果
}
