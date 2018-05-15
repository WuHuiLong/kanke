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
public class Hall {
    private Integer id;

    private String name;

    private Integer status;

    private Integer number;

    private String stype;

    private Date createTime;

    private Date updateTime;
}