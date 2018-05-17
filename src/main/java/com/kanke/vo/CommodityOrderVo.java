package com.kanke.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommodityOrderVo {

    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;
    private String paymentTypeDesc;

    private Integer status;
    private String statusDesc;

    private String paymentTime;

    private String endTime;

    private String closeTime;

    private String createTime;

    private String updateTime;

    private List<CommodityOrderItemVo> commodityOrderItemList;

    private String imageHost;
}
