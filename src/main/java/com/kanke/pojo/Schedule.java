package com.kanke.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private Integer id;

    private Integer movieId;

    private Integer hallId;

    private BigDecimal price;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;
}