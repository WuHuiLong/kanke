package com.kanke.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    private Integer id;

    private Integer hallId;

    private Integer column;

    private Integer row;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}