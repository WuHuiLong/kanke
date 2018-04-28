package com.kanke.vo;

import com.kanke.pojo.Seat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderVo {
    private Integer id;

    private Long orderNo;

    private Integer userId;

    private Integer scheduleId;

    private Integer quantity;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer status;

    private Date paymentTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;

    private Date updateTime;

    //后面是添加的
    private Integer movieId;

    private String movieName;

    private Integer hallId;

    private String hallName;

    private List<Seat> seatList;

    private List<OrderItemVo> orderItemVoList;
}